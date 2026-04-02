package com.neil.trantools.data.chat

import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.data.wiki.WikiArticle
import com.neil.trantools.domain.chat.ChatCoordinator
import com.neil.trantools.domain.chat.ChatEngine
import com.neil.trantools.domain.chat.LocalAssistantChatEngine
import com.neil.trantools.domain.chat.LocalRetrievalEngine

enum class ChatSourceType {
    Wiki,
    Gem,
    Map,
    History,
}

data class ChatSource(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: ChatSourceType,
    val detail: String = "",
)

data class LocalAssistantAnswer(
    val answer: String,
    val sources: List<ChatSource>,
    val suggestedQuestions: List<String> = emptyList(),
)

object LocalAssistantEngine {
    suspend fun answer(
        question: String,
        previousUserTurns: List<String>,
        wikiArticles: List<WikiArticle>,
        gems: List<GemPoi>,
        history: List<TranslationHistoryEntity>,
        fallbackAnswer: String,
        translateHistoryLabel: String,
        photoHistoryLabel: String,
        voiceHistoryLabel: String,
        chatEngineOverride: ChatEngine? = null,
        onPartialAnswer: (String) -> Unit = {},
    ): LocalAssistantAnswer {
        val coordinator = ChatCoordinator(
            retrievalEngine = LocalRetrievalEngine(
                wikiArticles = wikiArticles,
                gems = gems,
                history = history,
                translateHistoryLabel = translateHistoryLabel,
                photoHistoryLabel = photoHistoryLabel,
                voiceHistoryLabel = voiceHistoryLabel
            ),
            chatEngine = chatEngineOverride ?: LocalAssistantChatEngine(fallbackAnswer = fallbackAnswer)
        )
        return coordinator(
            question = question,
            previousUserTurns = previousUserTurns,
            onPartialAnswer = onPartialAnswer
        )
    }
}
