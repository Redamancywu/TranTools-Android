package com.neil.trantools.data.wiki

data class WikiAnswer(
    val question: String,
    val answer: String,
    val sourceIds: List<String>,
)

object WikiAnswerEngine {
    fun answer(
        question: String,
        articles: List<WikiArticle>,
        fallbackAnswer: String,
    ): WikiAnswer? {
        val normalizedQuestion = question.trim()
        if (normalizedQuestion.isEmpty()) return null

        val keywords = normalizedQuestion
            .lowercase()
            .split(Regex("[^\\p{L}\\p{N}]+"))
            .filter { it.length >= 2 }
            .toSet()

        val ranked = articles
            .map { article ->
                article to scoreArticle(article, keywords, normalizedQuestion)
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(3)

        if (ranked.isEmpty()) {
            return WikiAnswer(
                question = normalizedQuestion,
                answer = fallbackAnswer,
                sourceIds = emptyList()
            )
        }

        val topArticles = ranked.map { it.first }
        val summary = buildString {
            append(topArticles.first().summary)
            topArticles.drop(1).forEach { article ->
                append(' ')
                append(article.title)
                append(": ")
                append(article.summary)
            }
        }

        val supportingPoints = topArticles
            .flatMap { article -> article.content.take(1).map { "${article.title}: $it" } }
            .take(3)

        val answerText = buildString {
            append(summary)
            if (supportingPoints.isNotEmpty()) {
                append("\n\n")
                supportingPoints.forEachIndexed { index, point ->
                    append(index + 1)
                    append(". ")
                    append(point)
                    if (index != supportingPoints.lastIndex) append('\n')
                }
            }
        }

        return WikiAnswer(
            question = normalizedQuestion,
            answer = answerText,
            sourceIds = topArticles.map { it.id }
        )
    }

    private fun scoreArticle(
        article: WikiArticle,
        keywords: Set<String>,
        originalQuestion: String,
    ): Int {
        val haystack = buildString {
            append(article.title)
            append(' ')
            append(article.place)
            append(' ')
            append(article.summary)
            append(' ')
            append(article.fact)
            append(' ')
            append(article.content.joinToString(" "))
            append(' ')
            append(article.tags.joinToString(" "))
        }.lowercase()

        var score = 0
        keywords.forEach { keyword ->
            if (haystack.contains(keyword)) score += 3
        }
        if (haystack.contains(originalQuestion.lowercase())) {
            score += 5
        }
        if (article.title.lowercase() in originalQuestion.lowercase()) {
            score += 4
        }
        return score
    }
}
