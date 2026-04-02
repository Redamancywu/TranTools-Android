package com.neil.trantools.domain.chat

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.neil.trantools.data.chat.ChatSource
import com.neil.trantools.data.chat.LocalAssistantAnswer
import com.neil.trantools.data.settings.ResourcePackageRepository
import java.util.concurrent.atomic.AtomicReference

class MediaPipeAssistantChatEngine(
    private val context: Context,
    private val fallbackAnswer: String,
) : ChatEngine {
    @Volatile
    private var llmInference: LlmInference? = null

    @Volatile
    private var currentModelPath: String? = null

    override suspend fun generateAnswer(
        result: AssistantRetrievalResult,
        onPartialAnswer: (String) -> Unit,
    ): LocalAssistantAnswer {
        val modelPath = ResourcePackageRepository.getReadyModelPath(context)
            ?: return LocalAssistantChatEngine(fallbackAnswer).generateAnswer(result, onPartialAnswer)

        val inference = runCatching {
            ensureInference(modelPath)
        }.getOrElse {
            return LocalAssistantChatEngine(fallbackAnswer).generateAnswer(result, onPartialAnswer)
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
            future.get().trim()
        }.getOrElse {
            return LocalAssistantChatEngine(fallbackAnswer).generateAnswer(result, onPartialAnswer)
        }

        if (generatedText.isBlank()) {
            return LocalAssistantChatEngine(fallbackAnswer).generateAnswer(result, onPartialAnswer)
        }

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
}
