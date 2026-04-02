package com.neil.trantools.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.modelPackDataStore by preferencesDataStore(name = "model_pack_store")
private val installedModelPackIdsKey = stringSetPreferencesKey("installed_model_pack_ids")

object ModelPackStore {
    private val defaultInstalled = setOf("ocr-advanced", "assistant-local")

    fun observeInstalledIds(context: Context): Flow<Set<String>> {
        return context.modelPackDataStore.data.map { prefs ->
            prefs[installedModelPackIdsKey] ?: defaultInstalled
        }
    }

    suspend fun setInstalled(context: Context, packId: String, installed: Boolean) {
        context.modelPackDataStore.edit { prefs ->
            val current = (prefs[installedModelPackIdsKey] ?: defaultInstalled).toMutableSet()
            if (installed) {
                current.add(packId)
            } else {
                current.remove(packId)
            }
            prefs[installedModelPackIdsKey] = current
        }
    }
}
