package com.neil.trantools.data.chat

import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.data.wiki.WikiArticle

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
)

data class LocalAssistantAnswer(
    val answer: String,
    val sources: List<ChatSource>,
    val suggestedQuestions: List<String> = emptyList(),
)

object LocalAssistantEngine {
    fun answer(
        question: String,
        previousUserTurns: List<String>,
        wikiArticles: List<WikiArticle>,
        gems: List<GemPoi>,
        history: List<TranslationHistoryEntity>,
        fallbackAnswer: String,
        translateHistoryLabel: String,
        photoHistoryLabel: String,
        voiceHistoryLabel: String,
    ): LocalAssistantAnswer {
        val normalizedQuestion = question.trim()
        if (normalizedQuestion.isEmpty()) {
            return LocalAssistantAnswer(
                answer = fallbackAnswer,
                sources = emptyList(),
                suggestedQuestions = emptyList()
            )
        }

        val expandedQuestion = buildString {
            previousUserTurns.takeLast(2).forEach {
                append(it)
                append(' ')
            }
            append(normalizedQuestion)
        }
        val keywords = expandedQuestion
            .lowercase()
            .split(Regex("[^\\p{L}\\p{N}]+"))
            .filter { it.length >= 2 }
            .toSet()

        val wikiMatches = wikiArticles
            .map { article -> article to score(buildWikiHaystack(article), keywords, expandedQuestion) }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(2)

        val gemMatches = gems
            .map { gem -> gem to score(buildGemHaystack(gem), keywords, expandedQuestion) }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(2)

        val historyMatches = history
            .map { item -> item to score(buildHistoryHaystack(item), keywords, expandedQuestion) }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(2)

        if (wikiMatches.isEmpty() && gemMatches.isEmpty() && historyMatches.isEmpty()) {
            return LocalAssistantAnswer(
                answer = fallbackAnswer,
                sources = emptyList(),
                suggestedQuestions = emptyList()
            )
        }

        val paragraphs = buildList {
            wikiMatches.forEach { (article, _) ->
                add("${article.title}: ${article.summary}")
            }
            gemMatches.forEach { (gem, _) ->
                add("${gem.title}: ${gem.summary} ${gem.tip}")
            }
            historyMatches.forEach { (item, _) ->
                add("${item.sourceText} -> ${item.translatedText}")
            }
        }

        val answerText = buildString {
            append(paragraphs.first())
            if (paragraphs.size > 1) {
                append("\n\n")
                paragraphs.drop(1).forEachIndexed { index, text ->
                    append(index + 1)
                    append(". ")
                    append(text)
                    if (index != paragraphs.lastIndex - 1) append('\n')
                }
            }
        }

        val sources = buildList {
            wikiMatches.forEach { (article, _) ->
                add(
                    ChatSource(
                        id = article.id,
                        title = article.title,
                        subtitle = article.place,
                        type = ChatSourceType.Wiki
                    )
                )
            }
            gemMatches.forEach { (gem, _) ->
                add(
                    ChatSource(
                        id = gem.id,
                        title = gem.title,
                        subtitle = gem.address,
                        type = ChatSourceType.Map
                    )
                )
            }
            historyMatches.forEach { (item, _) ->
                add(
                    ChatSource(
                        id = item.id.toString(),
                        title = item.sourceText.take(28),
                        subtitle = when (item.mode) {
                            HistoryMode.TEXT -> translateHistoryLabel
                            HistoryMode.OCR -> photoHistoryLabel
                            HistoryMode.VOICE -> voiceHistoryLabel
                        },
                        type = ChatSourceType.History
                    )
                )
            }
        }

        val suggestedQuestions = buildList {
            wikiMatches.forEach { (article, _) ->
                add("Tell me more about ${article.title}.")
            }
            gemMatches.forEach { (gem, _) ->
                add("What should I not miss around ${gem.title}?")
            }
            historyMatches.firstOrNull()?.let { (item, _) ->
                add("Can you refine this translation: ${item.sourceText}?")
            }
        }.distinct().take(3)

        return LocalAssistantAnswer(
            answer = answerText,
            sources = sources,
            suggestedQuestions = suggestedQuestions
        )
    }

    private fun buildWikiHaystack(article: WikiArticle): String = buildString {
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

    private fun buildGemHaystack(gem: GemPoi): String = buildString {
        append(gem.title)
        append(' ')
        append(gem.summary)
        append(' ')
        append(gem.tip)
        append(' ')
        append(gem.address)
        append(' ')
        append(gem.tags.joinToString(" "))
    }.lowercase()

    private fun buildHistoryHaystack(item: TranslationHistoryEntity): String = buildString {
        append(item.sourceText)
        append(' ')
        append(item.translatedText)
    }.lowercase()

    private fun score(
        haystack: String,
        keywords: Set<String>,
        originalQuestion: String,
    ): Int {
        var score = 0
        keywords.forEach { keyword ->
            if (haystack.contains(keyword)) score += 3
        }
        if (haystack.contains(originalQuestion.lowercase())) score += 5
        return score
    }
}
