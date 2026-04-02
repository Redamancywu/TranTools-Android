package com.neil.trantools.feature.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.chat.ChatMessageEntity
import com.neil.trantools.data.chat.ChatSource
import com.neil.trantools.data.chat.ChatSourceType
import com.neil.trantools.data.chat.ChatStore
import com.neil.trantools.data.gems.GemsRepository
import com.neil.trantools.data.history.HistoryStore
import com.neil.trantools.data.wiki.WikiRepository
import com.neil.trantools.domain.chat.BuildLocalAssistantResponseUseCase
import com.neil.trantools.domain.chat.MediaPipeAssistantChatEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val appContext = getApplication<Application>().applicationContext
    private val messageId = AtomicLong(1L)
    private val generationSequence = AtomicLong(1L)
    private val mediaPipeAssistantChatEngine = MediaPipeAssistantChatEngine(
        context = appContext,
        fallbackAnswer = appContext.getString(R.string.chat_no_match)
    )
    private val buildAssistantResponse = BuildLocalAssistantResponseUseCase(
        wikiProvider = { question, limit -> WikiRepository.searchIndexedArticles(appContext, question, limit) },
        gemsProvider = { question, limit -> GemsRepository.searchIndexedPois(appContext, question, limit) },
        historyProvider = { limit -> HistoryStore.getRecent(limit) },
        fallbackAnswer = appContext.getString(R.string.chat_no_match),
        translateHistoryLabel = appContext.getString(R.string.chat_history_translate),
        photoHistoryLabel = appContext.getString(R.string.chat_history_photo),
        voiceHistoryLabel = appContext.getString(R.string.chat_history_voice),
        chatEngineProvider = { mediaPipeAssistantChatEngine }
    )
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    private var activeAnswerJob: Job? = null
    @Volatile
    private var activeGenerationId: Long = 0L

    init {
        viewModelScope.launch {
            loadPersistedMessages()
        }
        viewModelScope.launch(Dispatchers.IO) {
            mediaPipeAssistantChatEngine.warmUp()
        }
    }

    private suspend fun loadPersistedMessages() {
        val entities = withContext(Dispatchers.IO) { ChatStore.getAll() }
        if (entities.isEmpty()) {
            _uiState.value = ChatUiState(
                messages = listOf(
                    ChatMessage(
                        id = messageId.getAndIncrement(),
                        role = ChatRole.Assistant,
                        text = appContext.getString(R.string.chat_welcome)
                    )
                )
            )
            return
        }
        val messages = entities.map { it.toChatMessage() }
        val maxId = entities.maxOf { it.id }
        messageId.set(maxId + 1)
        _uiState.value = ChatUiState(messages = messages)
    }

    fun setInput(value: String) {
        _uiState.value = _uiState.value.copy(input = value)
    }

    fun applyPrefillQuestion(question: String) {
        _uiState.value = _uiState.value.copy(input = question)
        sendMessage(question)
    }

    fun sendCurrentInput() {
        sendMessage(_uiState.value.input)
    }

    fun sendSuggestedQuestion(question: String) {
        sendMessage(question)
    }

    fun stopGenerating() {
        val generationId = activeGenerationId
        if (generationId == 0L) return

        val partial = _uiState.value.streamingAnswer.trim()
        activeGenerationId = 0L
        activeAnswerJob?.cancel(CancellationException("User cancelled generation"))
        activeAnswerJob = null

        if (partial.isNotBlank()) {
            appendAssistantMessage(text = partial)
        }

        _uiState.update { state ->
            state.copy(
                isThinking = false,
                streamingAnswer = ""
            )
        }
    }

    private fun sendMessage(raw: String) {
        val question = raw.trim()
        if (question.isEmpty() || _uiState.value.isThinking) return

        val currentMessages = _uiState.value.messages
        val userMessage = ChatMessage(
            id = messageId.getAndIncrement(),
            role = ChatRole.User,
            text = question
        )
        _uiState.value = _uiState.value.copy(
            input = "",
            messages = currentMessages + userMessage,
            isThinking = true,
            streamingAnswer = ""
        )
        persistMessage(userMessage)

        val generationId = generationSequence.getAndIncrement()
        activeGenerationId = generationId

        activeAnswerJob = viewModelScope.launch {
            try {
                val answer = withContext(Dispatchers.IO) {
                    withTimeout(ASSISTANT_RESPONSE_TIMEOUT_MS) {
                        buildAssistantResponse(
                            question = question,
                            previousUserTurns = currentMessages.filter { it.role == ChatRole.User }.map { it.text },
                            onPartialAnswer = { partial ->
                                if (activeGenerationId != generationId) return@buildAssistantResponse
                                _uiState.update { state ->
                                    state.copy(
                                        streamingAnswer = partial,
                                        isThinking = true
                                    )
                                }
                            }
                        )
                    }
                }

                if (activeGenerationId != generationId) return@launch

                appendAssistantMessage(
                    text = answer.answer,
                    sources = answer.sources,
                    suggestedQuestions = answer.suggestedQuestions
                )
                _uiState.update { state ->
                    state.copy(
                        isThinking = false,
                        streamingAnswer = ""
                    )
                }
            } catch (timeout: TimeoutCancellationException) {
                if (activeGenerationId != generationId) return@launch
                val partial = _uiState.value.streamingAnswer.trim()
                appendAssistantMessage(
                    text = if (partial.isNotBlank()) partial else appContext.getString(R.string.chat_timeout)
                )
                _uiState.update { state ->
                    state.copy(
                        isThinking = false,
                        streamingAnswer = ""
                    )
                }
            } catch (_: CancellationException) {
                if (activeGenerationId != generationId) return@launch
                _uiState.update { state ->
                    state.copy(
                        isThinking = false,
                        streamingAnswer = ""
                    )
                }
            } finally {
                if (activeGenerationId == generationId) {
                    activeGenerationId = 0L
                    activeAnswerJob = null
                }
            }
        }
    }

    private fun appendAssistantMessage(
        text: String,
        sources: List<ChatSource> = emptyList(),
        suggestedQuestions: List<String> = emptyList(),
    ) {
        val normalizedText = text.trim()
        if (normalizedText.isBlank()) return
        val assistantMessage = ChatMessage(
            id = messageId.getAndIncrement(),
            role = ChatRole.Assistant,
            text = normalizedText,
            sources = sources,
            suggestedQuestions = suggestedQuestions
        )
        _uiState.update { state ->
            state.copy(messages = state.messages + assistantMessage)
        }
        persistMessage(assistantMessage)
    }

    private fun persistMessage(message: ChatMessage) {
        viewModelScope.launch(Dispatchers.IO) {
            ChatStore.insert(
                ChatMessageEntity(
                    id = message.id,
                    role = message.role.name,
                    text = message.text,
                    sourcesJson = serializeMetadata(message.sources, message.suggestedQuestions),
                    createdAtEpochMs = System.currentTimeMillis()
                )
            )
        }
    }
}

private fun ChatMessageEntity.toChatMessage(): ChatMessage {
    val metadata = deserializeMetadata(sourcesJson)
    return ChatMessage(
        id = id,
        role = runCatching { ChatRole.valueOf(role) }.getOrDefault(ChatRole.Assistant),
        text = text,
        sources = metadata.first,
        suggestedQuestions = metadata.second
    )
}

private fun serializeMetadata(
    sources: List<ChatSource>,
    suggestedQuestions: List<String>,
): String {
    val array = JSONArray()
    sources.forEach { source ->
        val obj = JSONObject().apply {
            put("id", source.id)
            put("title", source.title)
            put("subtitle", source.subtitle)
            put("type", source.type.name)
            put("detail", source.detail)
        }
        array.put(obj)
    }
    return JSONObject()
        .put("sources", array)
        .put("suggestedQuestions", JSONArray(suggestedQuestions))
        .toString()
}

private fun deserializeMetadata(json: String): Pair<List<ChatSource>, List<String>> {
    if (json.isBlank() || json == "[]") return emptyList<ChatSource>() to emptyList()
    return runCatching {
        val trimmed = json.trim()
        if (trimmed.startsWith("[")) {
            parseSources(JSONArray(trimmed)) to emptyList()
        } else {
            val obj = JSONObject(trimmed)
            val sources = parseSources(obj.optJSONArray("sources") ?: JSONArray())
            val suggestedQuestions = buildList {
                val array = obj.optJSONArray("suggestedQuestions") ?: JSONArray()
                for (index in 0 until array.length()) {
                    val question = array.optString(index).trim()
                    if (question.isNotEmpty()) add(question)
                }
            }
            sources to suggestedQuestions
        }
    }.getOrDefault(emptyList<ChatSource>() to emptyList())
}

private fun parseSources(array: JSONArray): List<ChatSource> {
    return (0 until array.length()).mapNotNull { i ->
        runCatching {
            val obj = array.getJSONObject(i)
            ChatSource(
                id = obj.getString("id"),
                title = obj.getString("title"),
                subtitle = obj.getString("subtitle"),
                detail = obj.optString("detail"),
                type = runCatching { ChatSourceType.valueOf(obj.getString("type")) }
                    .getOrDefault(ChatSourceType.Wiki)
            )
        }.getOrNull()
    }
}

private const val ASSISTANT_RESPONSE_TIMEOUT_MS = 45_000L
