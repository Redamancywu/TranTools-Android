package com.neil.trantools.data.wiki

import android.content.Context
import org.json.JSONArray
import java.util.Locale

object WikiRepository {
    private val cache = mutableMapOf<String, List<WikiArticle>>()

    fun loadArticles(context: Context): List<WikiArticle> {
        val language = currentLanguage(context)
        return cache.getOrPut(language) {
            val assetName = if (language.startsWith("zh")) {
                "wiki_articles_zh.json"
            } else {
                "wiki_articles_en.json"
            }

            context.assets.open(assetName).bufferedReader().use { reader ->
                parseArticles(reader.readText())
            }
        }
    }

    fun currentLanguage(context: Context): String {
        return context.resources.configuration.locales[0]?.language ?: Locale.getDefault().language
    }

    fun search(
        articles: List<WikiArticle>,
        query: String,
        category: WikiCategory?,
    ): List<WikiArticle> {
        val normalizedQuery = query.trim().lowercase()
        return articles.filter { article ->
            val matchesCategory = category == null || article.category == category
            val matchesQuery = normalizedQuery.isBlank() || buildString {
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
            }.lowercase().contains(normalizedQuery)

            matchesCategory && matchesQuery
        }
    }

    fun findById(
        articles: List<WikiArticle>,
        id: String,
    ): WikiArticle? = articles.firstOrNull { it.id == id }

    private fun parseArticles(json: String): List<WikiArticle> {
        val array = JSONArray(json)
        return buildList {
            for (index in 0 until array.length()) {
                val item = array.getJSONObject(index)
                add(
                    WikiArticle(
                        id = item.getString("id"),
                        title = item.getString("title"),
                        place = item.getString("place"),
                        category = WikiCategory.valueOf(item.getString("category")),
                        summary = item.getString("summary"),
                        fact = item.getString("fact"),
                        content = item.getJSONArray("content").toStringList(),
                        tags = item.getJSONArray("tags").toStringList()
                    )
                )
            }
        }
    }
}

private fun JSONArray.toStringList(): List<String> {
    return buildList {
        for (index in 0 until length()) {
            add(getString(index))
        }
    }
}
