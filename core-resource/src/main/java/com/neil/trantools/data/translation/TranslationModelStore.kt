package com.neil.trantools.data.translation

import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.neil.trantools.feature.translate.TranslateLanguageOption
import com.neil.trantools.feature.translate.supportedTranslateLanguages
import com.neil.trantools.feature.translate.toMlKitCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

data class TranslationPackStatus(
    val language: TranslateLanguageOption,
    val isDownloaded: Boolean = false,
    val isBusy: Boolean = false,
    val lastError: String? = null,
)

object TranslationModelStore {
    private val remoteModelManager = RemoteModelManager.getInstance()
    private val _statuses = MutableStateFlow(
        supportedTranslateLanguages.map { language ->
            TranslationPackStatus(language = language)
        }
    )

    fun observeStatuses(): StateFlow<List<TranslationPackStatus>> = _statuses.asStateFlow()

    suspend fun refresh() {
        val previous = _statuses.value.associateBy { it.language }
        val updated = withContext(Dispatchers.IO) {
            supportedTranslateLanguages.map { language ->
                val current = previous.getValue(language)
                val isDownloaded = runCatching {
                    Tasks.await(remoteModelManager.isModelDownloaded(modelFor(language)))
                }.getOrDefault(false)

                current.copy(
                    isDownloaded = isDownloaded,
                    isBusy = false,
                    lastError = if (isDownloaded) null else current.lastError
                )
            }
        }
        _statuses.value = updated
    }

    suspend fun download(language: TranslateLanguageOption) {
        updateStatus(language, isBusy = true, lastError = null)
        val result = withContext(Dispatchers.IO) {
            runCatching {
                Tasks.await(
                    remoteModelManager.download(
                        modelFor(language),
                        DownloadConditions.Builder().build()
                    )
                )
            }
        }
        if (result.isFailure) {
            updateStatus(
                language,
                isBusy = false,
                lastError = result.exceptionOrNull()?.message ?: "Download failed"
            )
            return
        }
        refresh()
    }

    suspend fun delete(language: TranslateLanguageOption) {
        updateStatus(language, isBusy = true, lastError = null)
        val result = withContext(Dispatchers.IO) {
            runCatching {
                Tasks.await(remoteModelManager.deleteDownloadedModel(modelFor(language)))
            }
        }
        if (result.isFailure) {
            updateStatus(
                language,
                isBusy = false,
                lastError = result.exceptionOrNull()?.message ?: "Delete failed"
            )
            return
        }
        refresh()
    }

    private fun updateStatus(
        language: TranslateLanguageOption,
        isBusy: Boolean,
        lastError: String?,
    ) {
        _statuses.update { list ->
            list.map { status ->
                if (status.language == language) {
                    status.copy(isBusy = isBusy, lastError = lastError)
                } else {
                    status
                }
            }
        }
    }

    private fun modelFor(language: TranslateLanguageOption): TranslateRemoteModel {
        val code = requireNotNull(language.toMlKitCode()) { "Auto does not map to a downloadable model" }
        return TranslateRemoteModel.Builder(code).build()
    }
}
