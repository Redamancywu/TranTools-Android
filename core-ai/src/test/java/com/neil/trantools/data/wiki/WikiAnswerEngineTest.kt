package com.neil.trantools.data.wiki

import org.junit.Assert.assertTrue
import org.junit.Test

class WikiAnswerEngineTest {
    @Test
    fun `answer includes matched article content`() {
        val article = WikiArticle(
            id = "onsen",
            title = "Onsen Etiquette",
            place = "Japan",
            category = WikiCategory.Culture,
            summary = "Onsen culture centers on bathing etiquette.",
            fact = "Wash before entering the bath.",
            content = listOf("Visitors are expected to wash thoroughly before entering the hot bath."),
            tags = listOf("Bathing", "Etiquette")
        )

        val answer = WikiAnswerEngine.answer(
            question = "What should I know before visiting an onsen?",
            articles = listOf(article),
            fallbackAnswer = "No match"
        )

        requireNotNull(answer)
        assertTrue(answer.answer.contains("Onsen Etiquette"))
        assertTrue(answer.sourceIds.contains("onsen"))
    }
}
