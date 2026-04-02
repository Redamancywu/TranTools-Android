package com.neil.trantools.data.gems

import android.content.Context
import com.neil.trantools.data.wiki.WikiAnswer
import com.neil.trantools.data.wiki.WikiAnswerEngine
import com.neil.trantools.data.wiki.WikiArticle
import com.neil.trantools.data.wiki.WikiRepository

data class GemWikiContext(
    val question: String,
    val answer: WikiAnswer,
    val sourceArticles: List<WikiArticle>,
)

object GemWikiBridge {
    fun buildContext(
        context: Context,
        poi: GemPoi,
        questionTemplate: (String) -> String,
        fallbackAnswer: String,
    ): GemWikiContext? {
        val wikiArticles = WikiRepository.loadArticles(context)
        val question = questionTemplate(poi.title)
        val enrichedQuestion = buildString {
            append(question)
            append(' ')
            append(poi.summary)
            append(' ')
            append(poi.tags.joinToString(" "))
        }
        val answer = WikiAnswerEngine.answer(
            question = enrichedQuestion,
            articles = wikiArticles,
            fallbackAnswer = fallbackAnswer
        ) ?: return null

        return GemWikiContext(
            question = question,
            answer = answer,
            sourceArticles = answer.sourceIds.mapNotNull { id -> WikiRepository.findById(wikiArticles, id) }
        )
    }
}
