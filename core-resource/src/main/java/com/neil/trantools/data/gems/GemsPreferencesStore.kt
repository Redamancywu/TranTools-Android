package com.neil.trantools.data.gems

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.gemsDataStore by preferencesDataStore(name = "gems_preferences")
private val favoriteGemIdsKey = stringSetPreferencesKey("favorite_gem_ids")

object GemsPreferencesStore {
    fun observeFavoriteIds(context: Context): Flow<Set<String>> {
        return context.gemsDataStore.data.map { prefs ->
            prefs[favoriteGemIdsKey].orEmpty()
        }
    }

    suspend fun toggleFavorite(context: Context, poiId: String) {
        context.gemsDataStore.edit { prefs ->
            val current = prefs[favoriteGemIdsKey].orEmpty().toMutableSet()
            if (!current.add(poiId)) {
                current.remove(poiId)
            }
            prefs[favoriteGemIdsKey] = current
        }
    }
}
