package com.neil.trantools.data.wiki

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.wikiDataStore by preferencesDataStore(name = "wiki_preferences")
private val favoriteIdsKey = stringSetPreferencesKey("favorite_article_ids")
private val recentIdsKey = stringPreferencesKey("recent_article_ids")

object WikiPreferencesStore {
    fun observeFavoriteIds(context: Context): Flow<Set<String>> {
        return context.wikiDataStore.data.map { prefs ->
            prefs[favoriteIdsKey].orEmpty()
        }
    }

    fun observeRecentIds(context: Context): Flow<List<String>> {
        return context.wikiDataStore.data.map { prefs ->
            prefs[recentIdsKey]
                .orEmpty()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
    }

    suspend fun toggleFavorite(context: Context, articleId: String) {
        context.wikiDataStore.edit { prefs ->
            val current = prefs[favoriteIdsKey].orEmpty().toMutableSet()
            if (!current.add(articleId)) {
                current.remove(articleId)
            }
            prefs[favoriteIdsKey] = current
        }
    }

    suspend fun addRecent(context: Context, articleId: String) {
        context.wikiDataStore.edit { prefs ->
            val updated = buildList {
                add(articleId)
                addAll(
                    prefs[recentIdsKey]
                        .orEmpty()
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() && it != articleId }
                )
            }.take(8)
            prefs[recentIdsKey] = updated.joinToString(",")
        }
    }
}
