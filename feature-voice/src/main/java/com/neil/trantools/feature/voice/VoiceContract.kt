package com.neil.trantools.feature.voice

import com.neil.trantools.feature.translate.TranslateLanguageOption

data class VoiceMessage(
    val sourceLanguage: TranslateLanguageOption,
    val targetLanguage: TranslateLanguageOption,
    val sourceText: String,
    val translatedText: String,
)

data class VoiceUiState(
    val hasMicPermission: Boolean = false,
    val isListening: Boolean = false,
    val sourceLanguage: TranslateLanguageOption = TranslateLanguageOption.English,
    val targetLanguage: TranslateLanguageOption = TranslateLanguageOption.Japanese,
    val autoSpeak: Boolean = true,
    val autoTurnTaking: Boolean = false,
    val shouldRestartListening: Boolean = false,
    val pendingTranscript: String = "",
    val messages: List<VoiceMessage> = emptyList(),
    val errorMessage: String? = null,
)
