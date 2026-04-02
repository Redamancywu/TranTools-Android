package com.neil.trantools.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.cityPackDataStore by preferencesDataStore(name = "city_pack_store")
private val installedCityPackIdsKey = stringSetPreferencesKey("installed_city_pack_ids")

object CityPackStore {
    private val defaultInstalled = setOf("kyoto")

    fun observeInstalledIds(context: Context): Flow<Set<String>> {
        return context.cityPackDataStore.data.map { prefs ->
            prefs[installedCityPackIdsKey] ?: defaultInstalled
        }
    }

    suspend fun setInstalled(context: Context, packId: String, installed: Boolean) {
        context.cityPackDataStore.edit { prefs ->
            val current = (prefs[installedCityPackIdsKey] ?: defaultInstalled).toMutableSet()
            if (installed) {
                current.add(packId)
            } else {
                current.remove(packId)
            }
            prefs[installedCityPackIdsKey] = current
        }
    }
}
