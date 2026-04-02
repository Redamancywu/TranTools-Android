package com.neil.trantools.feature.voice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.HistoryStore
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.data.settings.BehaviorPreferencesStore
import com.neil.trantools.data.translation.TranslationModelStore
import com.neil.trantools.domain.history.MapVoiceHistoryItemUseCase
import com.neil.trantools.feature.translate.TranslateLanguageOption
import com.neil.trantools.feature.translate.toMlKitCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private var translator: Translator? = null
    private var translatorPair: Pair<String, String>? = null
    private var recentlyCommittedFromManualStop: String? = null

    fun onMicPermissionChanged(granted: Boolean) {
        _uiState.value = _uiState.value.copy(hasMicPermission = granted)
    }

    fun setListening(listening: Boolean) {
        _uiState.value = _uiState.value.copy(isListening = listening)
    }

    fun cycleSourceLanguage() {
        val sequence = listOf(
            TranslateLanguageOption.English,
            TranslateLanguageOption.Japanese,
            TranslateLanguageOption.Chinese,
            TranslateLanguageOption.Korean,
            TranslateLanguageOption.Spanish
        )
        val current = _uiState.value.sourceLanguage
        val next = sequence[(sequence.indexOf(current).takeIf { it >= 0 } ?: 0).let { (it + 1) % sequence.size }]
        _uiState.value = _uiState.value.copy(sourceLanguage = next)
    }

    fun cycleTargetLanguage() {
        val sequence = listOf(
            TranslateLanguageOption.Japanese,
            TranslateLanguageOption.English,
            TranslateLanguageOption.Chinese,
            TranslateLanguageOption.Korean,
            TranslateLanguageOption.Spanish
        )
        val current = _uiState.value.targetLanguage
        val next = sequence[(sequence.indexOf(current).takeIf { it >= 0 } ?: 0).let { (it + 1) % sequence.size }]
        _uiState.value = _uiState.value.copy(targetLanguage = next)
    }

    fun swapLanguages() {
        _uiState.value = _uiState.value.copy(
            sourceLanguage = _uiState.value.targetLanguage,
            targetLanguage = _uiState.value.sourceLanguage
        )
    }

    fun setAutoSpeak(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoSpeak = enabled)
    }

    fun setAutoTurnTaking(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            autoTurnTaking = enabled,
            shouldRestartListening = false
        )
    }

    fun consumeRestartListening() {
        _uiState.value = _uiState.value.copy(shouldRestartListening = false)
    }

    fun applyHistoryItem(item: TranslationHistoryEntity) {
        val prefill = MapVoiceHistoryItemUseCase(item) ?: return
        _uiState.value = _uiState.value.copy(
            sourceLanguage = prefill.sourceLanguage,
            targetLanguage = prefill.targetLanguage,
            pendingTranscript = "",
            isListening = false,
            shouldRestartListening = false,
            messages = listOf(
                VoiceMessage(
                    sourceLanguage = prefill.sourceLanguage,
                    targetLanguage = prefill.targetLanguage,
                    sourceText = prefill.sourceText,
                    translatedText = prefill.translatedText
                )
            ),
            errorMessage = null
        )
    }

    fun onSpeechRecognized(sourceText: String) {
        val normalized = sourceText.trim()
        if (normalized.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = getApplication<Application>().getString(R.string.voice_error_no_speech)
            )
            return
        }
        val committed = recentlyCommittedFromManualStop
        if (committed != null && committed == normalized) {
            recentlyCommittedFromManualStop = null
            _uiState.value = _uiState.value.copy(
                pendingTranscript = "",
                isListening = false
            )
            return
        }

        _uiState.value = _uiState.value.copy(pendingTranscript = "")
        translateAndAppend(normalized)
    }

    fun onSpeechError(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun onPartialSpeech(partialText: String) {
        val normalized = partialText.trim()
        if (normalized.isEmpty()) return
        _uiState.value = _uiState.value.copy(pendingTranscript = normalized)
    }

    fun commitPendingSpeechIfAny() {
        val pending = _uiState.value.pendingTranscript.trim()
        _uiState.value = _uiState.value.copy(isListening = false, shouldRestartListening = false)
        if (pending.isBlank()) return
        recentlyCommittedFromManualStop = pending
        _uiState.value = _uiState.value.copy(pendingTranscript = "")
        translateAndAppend(pending)
    }

    private fun translateAndAppend(sourceText: String) {
        viewModelScope.launch {
            val offlineOnly = runCatching {
                BehaviorPreferencesStore.isOfflineOnly(getApplication())
            }.getOrDefault(false)
            val currentSourceLanguage = _uiState.value.sourceLanguage
            val currentTargetLanguage = _uiState.value.targetLanguage
            val autoTurn = _uiState.value.autoTurnTaking
            val sourceCode = currentSourceLanguage.toMlKitCode()
            val targetCode = currentTargetLanguage.toMlKitCode()

            val translated = if (sourceCode == null || targetCode == null || sourceCode == targetCode) {
                sourceText
            } else {
                runCatching {
                    withContext(Dispatchers.IO) {
                        val translatorClient = ensureTranslator(sourceCode, targetCode)
                        if (!offlineOnly) {
                            Tasks.await(translatorClient.downloadModelIfNeeded())
                        }
                        Tasks.await(translatorClient.translate(sourceText))
                    }
                }.getOrElse {
                    if (offlineOnly) {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = getApplication<Application>().getString(R.string.voice_error_offline_pack_missing)
                        )
                    }
                    sourceText
                }
            }

            val newMessage = VoiceMessage(
                sourceLanguage = currentSourceLanguage,
                targetLanguage = currentTargetLanguage,
                sourceText = sourceText,
                translatedText = translated
            )

            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + newMessage,
                sourceLanguage = if (autoTurn) currentTargetLanguage else currentSourceLanguage,
                targetLanguage = if (autoTurn) currentSourceLanguage else currentTargetLanguage,
                shouldRestartListening = autoTurn,
                errorMessage = null
            )

            runCatching {
                HistoryStore.insert(
                    TranslationHistoryEntity(
                        mode = HistoryMode.VOICE,
                        sourceLanguage = currentSourceLanguage.code,
                        targetLanguage = currentTargetLanguage.code,
                        sourceText = sourceText,
                        translatedText = translated
                    )
                )
            }

            runCatching { TranslationModelStore.refresh() }
        }
    }

    private fun ensureTranslator(
        sourceLanguage: String,
        targetLanguage: String,
    ): Translator {
        val requestedPair = sourceLanguage to targetLanguage
        val existing = translator
        if (existing != null && translatorPair == requestedPair) return existing

        existing?.close()

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        return Translation.getClient(options).also {
            translator = it
            translatorPair = requestedPair
        }
    }

    override fun onCleared() {
        super.onCleared()
        translator?.close()
    }
}
