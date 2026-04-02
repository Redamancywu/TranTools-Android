package com.neil.trantools.domain.chat

import com.neil.trantools.data.chat.ChatSource
import com.neil.trantools.data.chat.ChatSourceType
import com.neil.trantools.data.chat.LocalAssistantAnswer
import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.data.wiki.WikiArticle
import kotlin.math.max

enum class AssistantIntent {
    Knowledge,
    Nearby,
    Translation,
    Mixed,
}

enum class AssistantDomain {
    Wiki,
    Nearby,
    History,
}

data class AssistantRetrievalRequest(
    val question: String,
    val previousUserTurns: List<String> = emptyList(),
    val preferredDomains: Set<AssistantDomain> = AssistantDomain.entries.toSet(),
    val maxResults: Int = 4,
)

data class AssistantCitation(
    val id: String,
    val title: String,
    val subtitle: String,
    val excerpt: String,
    val type: ChatSourceType,
    val score: Int,
)

data class AssistantRetrievalResult(
    val question: String,
    val intent: AssistantIntent,
    val citations: List<AssistantCitation>,
)

interface RetrievalEngine {
    suspend fun retrieve(request: AssistantRetrievalRequest): AssistantRetrievalResult
}

interface ChatEngine {
    suspend fun generateAnswer(
        result: AssistantRetrievalResult,
        onPartialAnswer: (String) -> Unit = {},
    ): LocalAssistantAnswer
}

class ChatCoordinator(
    private val retrievalEngine: RetrievalEngine,
    private val chatEngine: ChatEngine,
) {
    suspend operator fun invoke(
        question: String,
        previousUserTurns: List<String>,
        preferredDomains: Set<AssistantDomain> = AssistantDomain.entries.toSet(),
        onPartialAnswer: (String) -> Unit = {},
    ): LocalAssistantAnswer {
        val result = retrievalEngine.retrieve(
            AssistantRetrievalRequest(
                question = question,
                previousUserTurns = previousUserTurns,
                preferredDomains = preferredDomains
            )
        )
        return chatEngine.generateAnswer(result, onPartialAnswer)
    }
}

class LocalRetrievalEngine(
    private val wikiArticles: List<WikiArticle>,
    private val gems: List<GemPoi>,
    private val history: List<TranslationHistoryEntity>,
    private val translateHistoryLabel: String,
    private val photoHistoryLabel: String,
    private val voiceHistoryLabel: String,
) : RetrievalEngine {
    override suspend fun retrieve(request: AssistantRetrievalRequest): AssistantRetrievalResult {
        val normalizedQuestion = request.question.trim()
        val expandedQuestion = buildExpandedQuestion(
            question = normalizedQuestion,
            previousUserTurns = request.previousUserTurns
        )
        val intent = classifyIntent(expandedQuestion)
        val keywords = tokenize(expandedQuestion)

        val citations = buildList {
            if (AssistantDomain.Wiki in request.preferredDomains) {
                addAll(rankWikiCitations(expandedQuestion, keywords, wikiArticles, maxResults = request.maxResults))
            }
            if (AssistantDomain.Nearby in request.preferredDomains) {
                addAll(rankPoiCitations(expandedQuestion, keywords, gems, maxResults = request.maxResults, intent = intent))
            }
            if (AssistantDomain.History in request.preferredDomains) {
                addAll(
                    rankHistoryCitations(
                        expandedQuestion = expandedQuestion,
                        keywords = keywords,
                        history = history,
                        translateHistoryLabel = translateHistoryLabel,
                        photoHistoryLabel = photoHistoryLabel,
                        voiceHistoryLabel = voiceHistoryLabel,
                        maxResults = request.maxResults,
                        intent = intent
                    )
                )
            }
        }
            .sortedByDescending { it.score }
            .distinctBy { "${it.type}:${it.id}" }
            .take(request.maxResults)

        return AssistantRetrievalResult(
            question = normalizedQuestion,
            intent = intent,
            citations = citations
        )
    }
}

class LocalAssistantChatEngine(
    private val fallbackAnswer: String,
) : ChatEngine {
    override suspend fun generateAnswer(
        result: AssistantRetrievalResult,
        onPartialAnswer: (String) -> Unit,
    ): LocalAssistantAnswer {
        if (result.citations.isEmpty()) {
            return LocalAssistantAnswer(
                answer = fallbackAnswer,
                sources = emptyList(),
                suggestedQuestions = emptyList()
            )
        }

        val answer = buildAnswer(result)
        onPartialAnswer(answer)
        val sources = result.citations.map { citation ->
            ChatSource(
                id = citation.id,
                title = citation.title,
                subtitle = citation.subtitle,
                type = citation.type,
                detail = citation.excerpt
            )
        }

        return LocalAssistantAnswer(
            answer = answer,
            sources = sources,
            suggestedQuestions = buildSuggestedQuestions(result)
        )
    }

    private fun buildAnswer(result: AssistantRetrievalResult): String {
        val intro = when (result.intent) {
            AssistantIntent.Nearby ->
                "Based on your local nearby and knowledge packs, these are the strongest matches."

            AssistantIntent.Translation ->
                "Based on your local packs and recent translation context, here is the most relevant guidance."

            AssistantIntent.Mixed ->
                "I combined local knowledge, nearby places, and recent context to answer this."

            AssistantIntent.Knowledge ->
                "Here is the best answer I can assemble from your local knowledge packs."
        }

        return buildString {
            append(intro)
            append("\n\n")
            result.citations.forEachIndexed { index, citation ->
                append(index + 1)
                append(". ")
                append(
                    when (citation.type) {
                        ChatSourceType.Wiki -> "${citation.title}: ${citation.excerpt}"
                        ChatSourceType.Gem,
                        ChatSourceType.Map -> "${citation.title}: ${citation.excerpt}"
                        ChatSourceType.History -> "Recent translation: ${citation.excerpt}"
                    }
                )
                append(" (")
                append(index + 1)
                append(")")
                if (index != result.citations.lastIndex) append('\n')
            }
        }
    }

    private fun buildSuggestedQuestions(result: AssistantRetrievalResult): List<String> {
        return buildList {
            result.citations.forEach { citation ->
                when (citation.type) {
                    ChatSourceType.Wiki -> {
                        add("Tell me more about ${citation.title}.")
                        add("What should I know before visiting ${citation.title}?")
                    }

                    ChatSourceType.Gem,
                    ChatSourceType.Map -> {
                        add("What should I not miss around ${citation.title}?")
                        add("How much time should I plan for ${citation.title}?")
                    }

                    ChatSourceType.History -> {
                        add("Can you refine this translation for me?")
                    }
                }
            }
        }.distinct().take(3)
    }
}

internal fun rankWikiCitations(
    expandedQuestion: String,
    keywords: Set<String>,
    articles: List<WikiArticle>,
    maxResults: Int,
): List<AssistantCitation> {
    return articles.mapNotNull { article ->
        val metadata = buildString {
            append(article.title)
            append(' ')
            append(article.place)
            append(' ')
            append(article.category.name)
            append(' ')
            append(article.tags.joinToString(" "))
        }.lowercase()
        val snippets = listOf(article.summary, article.fact) + article.content.take(2)
        val bestSnippet = snippets
            .map { snippet ->
                snippet to scoreCandidate(
                    metadata = metadata,
                    primaryTitle = article.title,
                    snippet = snippet,
                    expandedQuestion = expandedQuestion,
                    keywords = keywords
                )
            }
            .maxByOrNull { it.second }
            ?: return@mapNotNull null

        if (bestSnippet.second <= 0) return@mapNotNull null

        AssistantCitation(
            id = article.id,
            title = article.title,
            subtitle = article.place,
            excerpt = bestSnippet.first,
            type = ChatSourceType.Wiki,
            score = bestSnippet.second
        )
    }
        .sortedByDescending { it.score }
        .take(maxResults)
}

private fun rankPoiCitations(
    expandedQuestion: String,
    keywords: Set<String>,
    pois: List<GemPoi>,
    maxResults: Int,
    intent: AssistantIntent,
): List<AssistantCitation> {
    return pois.mapNotNull { poi ->
        val metadata = buildString {
            append(poi.title)
            append(' ')
            append(poi.category.name)
            append(' ')
            append(poi.address)
            append(' ')
            append(poi.tags.joinToString(" "))
        }.lowercase()
        val snippets = listOf(poi.summary, poi.tip, poi.bestTime, poi.recommendedDuration)
            .filter { it.isNotBlank() }
        val bestSnippet = snippets
            .map { snippet ->
                snippet to scoreCandidate(
                    metadata = metadata,
                    primaryTitle = poi.title,
                    snippet = snippet,
                    expandedQuestion = expandedQuestion,
                    keywords = keywords
                ) + if (intent == AssistantIntent.Nearby || intent == AssistantIntent.Mixed) 3 else 0
            }
            .maxByOrNull { it.second }
            ?: return@mapNotNull null

        if (bestSnippet.second <= 0) return@mapNotNull null

        AssistantCitation(
            id = poi.id,
            title = poi.title,
            subtitle = poi.address,
            excerpt = bestSnippet.first,
            type = ChatSourceType.Map,
            score = bestSnippet.second
        )
    }
        .sortedByDescending { it.score }
        .take(maxResults)
}

private fun rankHistoryCitations(
    expandedQuestion: String,
    keywords: Set<String>,
    history: List<TranslationHistoryEntity>,
    translateHistoryLabel: String,
    photoHistoryLabel: String,
    voiceHistoryLabel: String,
    maxResults: Int,
    intent: AssistantIntent,
): List<AssistantCitation> {
    return history.mapNotNull { item ->
        val snippet = "${item.sourceText} -> ${item.translatedText}"
        val score = scoreCandidate(
            metadata = snippet.lowercase(),
            primaryTitle = item.sourceText,
            snippet = snippet,
            expandedQuestion = expandedQuestion,
            keywords = keywords
        ) + if (intent == AssistantIntent.Translation || intent == AssistantIntent.Mixed) 4 else 0

        if (score <= 0) return@mapNotNull null

        AssistantCitation(
            id = item.id.toString(),
            title = item.sourceText.take(28),
            subtitle = when (item.mode) {
                HistoryMode.TEXT -> translateHistoryLabel
                HistoryMode.OCR -> photoHistoryLabel
                HistoryMode.VOICE -> voiceHistoryLabel
            },
            excerpt = snippet,
            type = ChatSourceType.History,
            score = score
        )
    }
        .sortedByDescending { it.score }
        .take(maxResults)
}

private fun buildExpandedQuestion(
    question: String,
    previousUserTurns: List<String>,
): String {
    return buildString {
        previousUserTurns.takeLast(2).forEach { turn ->
            append(turn.trim())
            append(' ')
        }
        append(question)
    }.trim()
}

private fun classifyIntent(question: String): AssistantIntent {
    val normalized = question.lowercase()
    val hasNearby = nearbyKeywords.any { keyword -> keyword in normalized }
    val hasTranslation = translationKeywords.any { keyword -> keyword in normalized }
    return when {
        hasNearby && hasTranslation -> AssistantIntent.Mixed
        hasNearby -> AssistantIntent.Nearby
        hasTranslation -> AssistantIntent.Translation
        else -> AssistantIntent.Knowledge
    }
}

private fun tokenize(value: String): Set<String> {
    return value
        .lowercase()
        .split(Regex("[^\\p{L}\\p{N}]+"))
        .filter { it.length >= 2 }
        .toSet()
}

private fun scoreCandidate(
    metadata: String,
    primaryTitle: String,
    snippet: String,
    expandedQuestion: String,
    keywords: Set<String>,
): Int {
    val normalizedQuestion = expandedQuestion.lowercase()
    val normalizedSnippet = snippet.lowercase()
    val normalizedTitle = primaryTitle.lowercase()
    var score = 0

    keywords.forEach { keyword ->
        if (metadata.contains(keyword)) score += 4
        if (normalizedSnippet.contains(keyword)) score += 3
    }

    if (normalizedQuestion.contains(normalizedTitle)) {
        score += 8
    }
    if (normalizedSnippet.contains(normalizedQuestion)) {
        score += 6
    }

    return max(score, 0)
}

private val nearbyKeywords = setOf(
    "nearby",
    "around",
    "close",
    "walk",
    "food",
    "eat",
    "cafe",
    "restaurant",
    "market",
    "spot",
    "visit",
    "附近",
    "周边",
    "吃",
    "去哪",
    "哪里",
    "景点"
)

private val translationKeywords = setOf(
    "translate",
    "translation",
    "say",
    "meaning",
    "polite",
    "phrase",
    "ask",
    "speak",
    "翻译",
    "怎么说",
    "礼貌",
    "表达",
    "说"
)
