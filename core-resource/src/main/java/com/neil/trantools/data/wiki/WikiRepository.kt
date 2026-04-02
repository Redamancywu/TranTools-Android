package com.neil.trantools.data.wiki

import android.content.Context
import com.neil.trantools.data.content.LocalContentStore
import com.neil.trantools.data.content.WikiArticleEntity
import com.neil.trantools.data.content.WikiArticleFtsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.util.Locale

object WikiRepository {
    private val cache = mutableMapOf<String, List<WikiArticle>>()

    fun loadArticles(context: Context): List<WikiArticle> {
        val language = currentLanguage(context)
        val indexedArticles = runCatching {
            runBlocking { LocalContentStore.getWikiArticles(language).map { it.toDomain() } }
        }.getOrDefault(emptyList())
        if (indexedArticles.isNotEmpty()) {
            cache[language] = indexedArticles
            return indexedArticles
        }
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

    suspend fun syncIndexIfNeeded(context: Context) {
        val language = currentLanguage(context)
        if (LocalContentStore.wikiCount(language) > 0) return
        val articles = withContext(Dispatchers.IO) {
            val assetName = if (language.startsWith("zh")) {
                "wiki_articles_zh.json"
            } else {
                "wiki_articles_en.json"
            }
            context.assets.open(assetName).bufferedReader().use { reader ->
                parseArticles(reader.readText())
            }
        }
        LocalContentStore.replaceWikiArticles(
            language = language,
            entities = articles.map { it.toEntity(language) },
            ftsEntities = articles.mapIndexed { index, article ->
                article.toFtsEntity(language = language, rowId = index + 1)
            }
        )
        cache[language] = articles
    }

    suspend fun searchIndexedArticles(
        context: Context,
        query: String,
        limit: Int = 12,
    ): List<WikiArticle> {
        syncIndexIfNeeded(context)
        val language = currentLanguage(context)
        val dbArticles = LocalContentStore.getWikiArticles(language)
        if (query.isBlank()) return dbArticles.map { it.toDomain() }.take(limit)
        val ids = LocalContentStore.searchWikiArticleIds(
            language = language,
            query = buildFtsQuery(query),
            limit = limit
        )
        if (ids.isEmpty()) return dbArticles.map { it.toDomain() }.take(limit)
        val byId = dbArticles.associateBy { it.id }
        return ids.mapNotNull { id -> byId[id]?.toDomain() }
    }

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

private fun WikiArticle.toEntity(language: String): WikiArticleEntity {
    return WikiArticleEntity(
        id = id,
        language = language,
        title = title,
        place = place,
        category = category.name,
        summary = summary,
        fact = fact,
        contentBlob = content.joinToString(separator = "\u001F"),
        tagsBlob = tags.joinToString(separator = "\u001F")
    )
}

private fun WikiArticle.toFtsEntity(
    language: String,
    rowId: Int,
): WikiArticleFtsEntity {
    return WikiArticleFtsEntity(
        rowId = rowId,
        articleId = id,
        language = language,
        title = title,
        place = place,
        summary = summary,
        fact = fact,
        content = content.joinToString(" "),
        tags = tags.joinToString(" ")
    )
}

private fun WikiArticleEntity.toDomain(): WikiArticle {
    return WikiArticle(
        id = id,
        title = title,
        place = place,
        category = WikiCategory.valueOf(category),
        summary = summary,
        fact = fact,
        content = contentBlob.split("\u001F").filter { it.isNotBlank() },
        tags = tagsBlob.split("\u001F").filter { it.isNotBlank() }
    )
}

private fun buildFtsQuery(raw: String): String {
    return raw.trim()
        .split(Regex("[^\\p{L}\\p{N}]+"))
        .filter { it.length >= 2 }
        .joinToString(" OR ") { token -> "$token*" }
        .ifBlank { raw.trim() }
}
