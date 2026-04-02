package com.neil.trantools.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.behaviorDataStore by preferencesDataStore(name = "behavior_preferences")

object BehaviorPreferencesStore {
    private val KEY_OFFLINE_ONLY = booleanPreferencesKey("offline_only")
    private val KEY_AUTO_DOWNLOAD_WIFI = booleanPreferencesKey("auto_download_wifi")
    private val KEY_HIGH_QUALITY_OCR = booleanPreferencesKey("high_quality_ocr")

    fun observeOfflineOnly(context: Context): Flow<Boolean> =
        context.behaviorDataStore.data.map { it[KEY_OFFLINE_ONLY] ?: true }

    fun observeAutoDownloadWifi(context: Context): Flow<Boolean> =
        context.behaviorDataStore.data.map { it[KEY_AUTO_DOWNLOAD_WIFI] ?: true }

    fun observeHighQualityOcr(context: Context): Flow<Boolean> =
        context.behaviorDataStore.data.map { it[KEY_HIGH_QUALITY_OCR] ?: false }

    suspend fun setOfflineOnly(context: Context, value: Boolean) {
        context.behaviorDataStore.edit { it[KEY_OFFLINE_ONLY] = value }
    }

    suspend fun setAutoDownloadWifi(context: Context, value: Boolean) {
        context.behaviorDataStore.edit { it[KEY_AUTO_DOWNLOAD_WIFI] = value }
    }

    suspend fun setHighQualityOcr(context: Context, value: Boolean) {
        context.behaviorDataStore.edit { it[KEY_HIGH_QUALITY_OCR] = value }
    }

    suspend fun isOfflineOnly(context: Context): Boolean {
        return context.behaviorDataStore.data
            .map { it[KEY_OFFLINE_ONLY] ?: true }
            .first()
    }
}
