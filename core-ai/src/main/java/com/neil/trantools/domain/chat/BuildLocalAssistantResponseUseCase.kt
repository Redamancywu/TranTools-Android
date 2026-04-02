package com.neil.trantools.domain.chat

import com.neil.trantools.data.chat.LocalAssistantAnswer
import com.neil.trantools.data.chat.LocalAssistantEngine
import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.data.wiki.WikiArticle

class BuildLocalAssistantResponseUseCase(
    private val wikiProvider: () -> List<WikiArticle>,
    private val gemsProvider: () -> List<GemPoi>,
    private val historyProvider: suspend (Int) -> List<TranslationHistoryEntity>,
    private val fallbackAnswer: String,
    private val translateHistoryLabel: String,
    private val photoHistoryLabel: String,
    private val voiceHistoryLabel: String,
) {
    suspend operator fun invoke(
        question: String,
        previousUserTurns: List<String>,
    ): LocalAssistantAnswer {
        return LocalAssistantEngine.answer(
            question = question,
            previousUserTurns = previousUserTurns,
            wikiArticles = wikiProvider(),
            gems = gemsProvider(),
            history = historyProvider(6),
            fallbackAnswer = fallbackAnswer,
            translateHistoryLabel = translateHistoryLabel,
            photoHistoryLabel = photoHistoryLabel,
            voiceHistoryLabel = voiceHistoryLabel
        )
    }
}
