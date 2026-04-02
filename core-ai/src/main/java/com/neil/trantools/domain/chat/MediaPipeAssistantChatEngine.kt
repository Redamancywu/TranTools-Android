package com.neil.trantools.domain.chat

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.neil.trantools.data.chat.ChatSource
import com.neil.trantools.data.chat.LocalAssistantAnswer
import com.neil.trantools.data.settings.ResourcePackageRepository
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

enum class AssistantModelRuntimeState {
    Unknown,
    NoModel,
    Ready,
    Failed,
}

data class AssistantModelRuntimeStatus(
    val state: AssistantModelRuntimeState,
    val message: String? = null,
)

class MediaPipeAssistantChatEngine(
    private val context: Context,
    private val fallbackAnswer: String,
) : ChatEngine {
    @Volatile
    private var llmInference: LlmInference? = null

    @Volatile
    private var currentModelPath: String? = null
    @Volatile
    private var runtimeStatus = AssistantModelRuntimeStatus(state = AssistantModelRuntimeState.Unknown)

    override suspend fun generateAnswer(
        result: AssistantRetrievalResult,
        onPartialAnswer: (String) -> Unit,
    ): LocalAssistantAnswer {
        val modelPath = ResourcePackageRepository.getReadyModelPath(context)
            ?: return fallbackWithStatus(
                result = result,
                onPartialAnswer = onPartialAnswer,
                state = AssistantModelRuntimeState.NoModel,
                message = "Model pack not installed."
            )

        val inference = runCatching {
            ensureInference(modelPath)
        }.getOrElse {
            if (it is CancellationException) throw it
            return fallbackWithStatus(
                result = result,
                onPartialAnswer = onPartialAnswer,
                state = AssistantModelRuntimeState.Failed,
                message = it.message ?: "Model initialization failed."
            )
        }

        val prompt = buildPrompt(result)
        val partialAnswer = AtomicReference("")
        val generatedText = runCatching {
            val future = inference.generateResponseAsync(prompt) { chunk, _ ->
                val merged = mergePartialAnswer(
                    previous = partialAnswer.get(),
                    incoming = chunk.orEmpty()
                )
                if (merged != partialAnswer.get()) {
                    partialAnswer.set(merged)
                    onPartialAnswer(merged.trim())
                }
            }
            try {
                future.get(MODEL_RESPONSE_TIMEOUT_SECONDS, TimeUnit.SECONDS).trim()
            } catch (timeout: TimeoutException) {
                future.cancel(true)
                throw timeout
            }
        }.getOrElse {
            if (it is CancellationException) throw it
            return fallbackWithStatus(
                result = result,
                onPartialAnswer = onPartialAnswer,
                state = AssistantModelRuntimeState.Failed,
                message = if (it is TimeoutException) {
                    "Model inference timed out."
                } else {
                    it.message ?: "Model inference failed."
                }
            )
        }

        if (generatedText.isBlank()) {
            return fallbackWithStatus(
                result = result,
                onPartialAnswer = onPartialAnswer,
                state = AssistantModelRuntimeState.Failed,
                message = "Model returned empty response."
            )
        }

        setRuntimeStatus(AssistantModelRuntimeState.Ready)
        onPartialAnswer(generatedText)

        return LocalAssistantAnswer(
            answer = generatedText,
            sources = result.citations.map { citation ->
                ChatSource(
                    id = citation.id,
                    title = citation.title,
                    subtitle = citation.subtitle,
                    type = citation.type,
                    detail = citation.excerpt
                )
            },
            suggestedQuestions = buildSuggestedQuestions(result)
        )
    }

    suspend fun warmUp(): AssistantModelRuntimeStatus {
        val modelPath = ResourcePackageRepository.getReadyModelPath(context)
        if (modelPath == null) {
            return setRuntimeStatus(
                state = AssistantModelRuntimeState.NoModel,
                message = "Model pack not installed."
            )
        }

        return runCatching {
            ensureInference(modelPath)
            setRuntimeStatus(AssistantModelRuntimeState.Ready)
        }.getOrElse { throwable ->
            setRuntimeStatus(
                state = AssistantModelRuntimeState.Failed,
                message = throwable.message ?: "Model warm-up failed."
            )
        }
    }

    fun getRuntimeStatus(): AssistantModelRuntimeStatus = runtimeStatus

    private suspend fun ensureInference(modelPath: String): LlmInference {
        val existing = llmInference
        if (existing != null && currentModelPath == modelPath) {
            return existing
        }

        llmInference?.close()
        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
            .setMaxTokens(512)
            .setMaxTopK(40)
            .build()
        return LlmInference.createFromOptions(context, options).also { inference ->
            llmInference = inference
            currentModelPath = modelPath
        }
    }

    private suspend fun fallbackWithStatus(
        result: AssistantRetrievalResult,
        onPartialAnswer: (String) -> Unit,
        state: AssistantModelRuntimeState,
        message: String,
    ): LocalAssistantAnswer {
        setRuntimeStatus(state = state, message = message)
        return LocalAssistantChatEngine(fallbackAnswer).generateAnswer(result, onPartialAnswer)
    }

    private fun setRuntimeStatus(
        state: AssistantModelRuntimeState,
        message: String? = null,
    ): AssistantModelRuntimeStatus {
        return AssistantModelRuntimeStatus(
            state = state,
            message = message
        ).also { status ->
            runtimeStatus = status
        }
    }

    private fun buildPrompt(result: AssistantRetrievalResult): String {
        return buildString {
            appendLine("You are TranTools, an offline travel assistant.")
            appendLine("Use only the provided local sources. If the sources are insufficient, say so clearly.")
            appendLine("Keep the answer concise, helpful, and grounded.")
            appendLine("Mention source numbers in parentheses when making factual claims.")
            appendLine()
            appendLine("User question:")
            appendLine(result.question)
            appendLine()
            appendLine("Local sources:")
            result.citations.forEachIndexed { index, citation ->
                append('[')
                append(index + 1)
                append("] ")
                append(citation.title)
                append(" | ")
                append(citation.subtitle)
                appendLine()
                appendLine(citation.excerpt)
                appendLine()
            }
        }
    }

    private fun buildSuggestedQuestions(result: AssistantRetrievalResult): List<String> {
        return buildList {
            result.citations.forEach { citation ->
                add("Tell me more about ${citation.title}.")
            }
        }.distinct().take(3)
    }

    private fun mergePartialAnswer(
        previous: String,
        incoming: String,
    ): String {
        if (incoming.isBlank()) return previous
        if (previous.isBlank()) return incoming
        if (incoming.startsWith(previous)) return incoming
        if (previous.startsWith(incoming)) return previous
        if (previous.contains(incoming)) return previous
        return previous + incoming
    }

    companion object {
        private const val MODEL_RESPONSE_TIMEOUT_SECONDS = 40L
    }
}
