package com.neil.trantools.feature.translate

import com.neil.trantools.data.translation.TranslationPackStatus
import com.neil.trantools.data.translation.FavoritePhrase

enum class TranslateInputMode {
    Text,
    Camera
}

data class RecognizedRegion(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
)

data class TranslateUiState(
    val inputMode: TranslateInputMode = TranslateInputMode.Text,
    val hasCameraPermission: Boolean = false,
    val isProcessing: Boolean = false,
    val sourceLanguage: TranslateLanguageOption = TranslateLanguageOption.Japanese,
    val targetLanguage: TranslateLanguageOption = TranslateLanguageOption.English,
    val detectedTextSourceLanguage: TranslateLanguageOption? = null,
    val textInput: String = "",
    val textTranslation: String = "",
    val favoritePhrases: List<FavoritePhrase> = emptyList(),
    val commonPhrases: List<FavoritePhrase> = emptyList(),
    val ocrInsight: String? = null,
    val recognizedLines: List<String> = emptyList(),
    val translatedLines: List<String> = emptyList(),
    val recognizedRegions: List<RecognizedRegion> = emptyList(),
    val errorMessage: String? = null,
)

fun TranslateUiState.effectiveTextSourceLanguage(): TranslateLanguageOption? {
    return when (sourceLanguage) {
        TranslateLanguageOption.Auto -> detectedTextSourceLanguage
        else -> sourceLanguage
    }
}
