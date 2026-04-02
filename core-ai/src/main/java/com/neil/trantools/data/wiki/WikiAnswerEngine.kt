package com.neil.trantools.data.wiki

import com.neil.trantools.domain.chat.rankWikiCitations

data class WikiAnswerSource(
    val articleId: String,
    val title: String,
    val subtitle: String,
    val excerpt: String,
)

data class WikiAnswer(
    val question: String,
    val answer: String,
    val sourceIds: List<String>,
    val sources: List<WikiAnswerSource> = emptyList(),
    val suggestedQuestions: List<String> = emptyList(),
)

object WikiAnswerEngine {
    fun answer(
        question: String,
        articles: List<WikiArticle>,
        fallbackAnswer: String,
    ): WikiAnswer? {
        val normalizedQuestion = question.trim()
        if (normalizedQuestion.isEmpty()) return null

        val citations = rankWikiCitations(
            expandedQuestion = normalizedQuestion,
            keywords = normalizedQuestion
                .lowercase()
                .split(Regex("[^\\p{L}\\p{N}]+"))
                .filter { it.length >= 2 }
                .toSet(),
            articles = articles,
            maxResults = 3
        )

        if (citations.isEmpty()) {
            return WikiAnswer(
                question = normalizedQuestion,
                answer = fallbackAnswer,
                sourceIds = emptyList(),
                sources = emptyList(),
                suggestedQuestions = emptyList()
            )
        }

        val answerText = buildString {
            append("Here is what your local knowledge pack says.")
            append("\n\n")
            citations.forEachIndexed { index, citation ->
                append(index + 1)
                append(". ")
                append(citation.title)
                append(": ")
                append(citation.excerpt)
                if (index != citations.lastIndex) append('\n')
            }
        }

        val suggestedQuestions = buildList {
            citations.forEach { citation ->
                add("What should I know before visiting ${citation.title}?")
                add("Any local etiquette or timing tips for ${citation.title}?")
            }
        }.distinct().take(3)

        return WikiAnswer(
            question = normalizedQuestion,
            answer = answerText,
            sourceIds = citations.map { it.id },
            sources = citations.map { citation ->
                WikiAnswerSource(
                    articleId = citation.id,
                    title = citation.title,
                    subtitle = citation.subtitle,
                    excerpt = citation.excerpt
                )
            },
            suggestedQuestions = suggestedQuestions
        )
    }
}
