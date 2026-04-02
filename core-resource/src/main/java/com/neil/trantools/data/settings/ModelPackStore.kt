package com.neil.trantools.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.modelPackDataStore by preferencesDataStore(name = "model_pack_store")
private val legacyInstalledModelPackIdsKey = stringSetPreferencesKey("installed_model_pack_ids")
private val modelPackRecordsJsonKey = stringPreferencesKey("model_pack_records_json")

enum class ModelPackInstallStatus {
    NotInstalled,
    Downloading,
    Ready,
    Failed,
}

data class ModelPackRecord(
    val id: String,
    val installStatus: ModelPackInstallStatus = ModelPackInstallStatus.NotInstalled,
    val progressPercent: Int = 0,
    val localPath: String? = null,
    val errorMessage: String? = null,
) {
    val installed: Boolean
        get() = installStatus == ModelPackInstallStatus.Ready
}

object ModelPackStore {
    fun observeRecords(context: Context): Flow<Map<String, ModelPackRecord>> {
        return context.modelPackDataStore.data.map { prefs ->
            val rawJson = prefs[modelPackRecordsJsonKey]
            if (!rawJson.isNullOrBlank()) {
                parseRecords(rawJson)
            } else {
                prefs[legacyInstalledModelPackIdsKey]
                    .orEmpty()
                    .associateWith { legacyId ->
                        ModelPackRecord(
                            id = legacyId,
                            installStatus = ModelPackInstallStatus.Ready,
                            progressPercent = 100
                        )
                    }
            }
        }
    }

    fun observeInstalledIds(context: Context): Flow<Set<String>> {
        return observeRecords(context).map { records ->
            records.values
                .filter { it.installed }
                .map { it.id }
                .toSet()
        }
    }

    suspend fun getRecord(
        context: Context,
        packId: String,
    ): ModelPackRecord? {
        return observeRecords(context).first()[packId]
    }

    suspend fun setInstalled(
        context: Context,
        packId: String,
        installed: Boolean,
        localPath: String? = null,
    ) {
        updateRecord(context, packId) { current ->
            if (installed) {
                current.copy(
                    installStatus = ModelPackInstallStatus.Ready,
                    progressPercent = 100,
                    localPath = localPath ?: current.localPath,
                    errorMessage = null
                )
            } else {
                current.copy(
                    installStatus = ModelPackInstallStatus.NotInstalled,
                    progressPercent = 0,
                    localPath = null,
                    errorMessage = null
                )
            }
        }
    }

    suspend fun setDownloading(
        context: Context,
        packId: String,
        progressPercent: Int,
    ) {
        updateRecord(context, packId) { current ->
            current.copy(
                installStatus = ModelPackInstallStatus.Downloading,
                progressPercent = progressPercent.coerceIn(0, 99),
                errorMessage = null
            )
        }
    }

    suspend fun setFailed(
        context: Context,
        packId: String,
        errorMessage: String,
    ) {
        updateRecord(context, packId) { current ->
            current.copy(
                installStatus = ModelPackInstallStatus.Failed,
                progressPercent = 0,
                errorMessage = errorMessage
            )
        }
    }

    private suspend fun updateRecord(
        context: Context,
        packId: String,
        transform: (ModelPackRecord) -> ModelPackRecord,
    ) {
        context.modelPackDataStore.edit { prefs ->
            val current = parseRecords(prefs[modelPackRecordsJsonKey]).toMutableMap()
            val existing = current[packId] ?: ModelPackRecord(id = packId)
            current[packId] = transform(existing)
            prefs[modelPackRecordsJsonKey] = serializeRecords(current)
        }
    }

    private fun parseRecords(rawJson: String?): Map<String, ModelPackRecord> {
        if (rawJson.isNullOrBlank()) return emptyMap()
        return runCatching {
            val array = JSONArray(rawJson)
            buildMap {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)
                    val record = ModelPackRecord(
                        id = item.getString("id"),
                        installStatus = runCatching {
                            ModelPackInstallStatus.valueOf(item.getString("installStatus"))
                        }.getOrDefault(ModelPackInstallStatus.NotInstalled),
                        progressPercent = item.optInt("progressPercent", 0),
                        localPath = item.optString("localPath").takeIf { it.isNotBlank() },
                        errorMessage = item.optString("errorMessage").takeIf { it.isNotBlank() }
                    )
                    put(record.id, record)
                }
            }
        }.getOrDefault(emptyMap())
    }

    private fun serializeRecords(records: Map<String, ModelPackRecord>): String {
        val array = JSONArray()
        records.values.sortedBy { it.id }.forEach { record ->
            array.put(
                JSONObject().apply {
                    put("id", record.id)
                    put("installStatus", record.installStatus.name)
                    put("progressPercent", record.progressPercent)
                    put("localPath", record.localPath.orEmpty())
                    put("errorMessage", record.errorMessage.orEmpty())
                }
            )
        }
        return array.toString()
    }
}
