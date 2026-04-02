package com.neil.trantools.feature.wiki

import com.neil.trantools.data.wiki.WikiArticle
import com.neil.trantools.data.wiki.WikiAnswer
import com.neil.trantools.data.wiki.WikiCategory
import com.neil.trantools.data.wiki.WikiRepository

data class WikiUiState(
    val allArticles: List<WikiArticle> = emptyList(),
    val query: String = "",
    val question: String = "",
    val answer: WikiAnswer? = null,
    val selectedCategory: WikiCategory? = null,
    val favorites: Set<String> = emptySet(),
    val recentIds: List<String> = emptyList(),
) {
    val articles: List<WikiArticle>
        get() = WikiRepository.search(articles = allArticles, query = query, category = selectedCategory)

    val featuredArticle: WikiArticle?
        get() = articles.firstOrNull()

    val recentArticles: List<WikiArticle>
        get() = recentIds.mapNotNull { id -> WikiRepository.findById(allArticles, id) }

    val favoriteArticles: List<WikiArticle>
        get() = allArticles.filter { it.id in favorites }
}
