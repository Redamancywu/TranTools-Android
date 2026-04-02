package com.neil.trantools.domain.chat

import com.neil.trantools.data.chat.LocalAssistantAnswer
import com.neil.trantools.data.chat.LocalAssistantEngine
import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.data.wiki.WikiArticle

class BuildLocalAssistantResponseUseCase(
    private val wikiProvider: suspend (String, Int) -> List<WikiArticle>,
    private val gemsProvider: suspend (String, Int) -> List<GemPoi>,
    private val historyProvider: suspend (Int) -> List<TranslationHistoryEntity>,
    private val fallbackAnswer: String,
    private val translateHistoryLabel: String,
    private val photoHistoryLabel: String,
    private val voiceHistoryLabel: String,
    private val chatEngineProvider: (() -> ChatEngine?)? = null,
) {
    suspend operator fun invoke(
        question: String,
        previousUserTurns: List<String>,
        onPartialAnswer: (String) -> Unit = {},
    ): LocalAssistantAnswer {
        return LocalAssistantEngine.answer(
            question = question,
            previousUserTurns = previousUserTurns,
            wikiArticles = wikiProvider(question, 12),
            gems = gemsProvider(question, 12),
            history = historyProvider(6),
            fallbackAnswer = fallbackAnswer,
            translateHistoryLabel = translateHistoryLabel,
            photoHistoryLabel = photoHistoryLabel,
            voiceHistoryLabel = voiceHistoryLabel,
            chatEngineOverride = chatEngineProvider?.invoke(),
            onPartialAnswer = onPartialAnswer
        )
    }
}
