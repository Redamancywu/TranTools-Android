package com.neil.trantools.domain.history

import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.feature.translate.TranslateLanguageOption
import com.neil.trantools.feature.translate.translateLanguageFromStored

data class TranslateHistoryPrefill(
    val isTextMode: Boolean,
    val sourceLanguage: TranslateLanguageOption,
    val targetLanguage: TranslateLanguageOption,
    val detectedSourceLanguage: TranslateLanguageOption?,
    val textInput: String,
    val textTranslation: String,
    val recognizedLines: List<String>,
    val translatedLines: List<String>,
)

data class VoiceHistoryPrefill(
    val sourceLanguage: TranslateLanguageOption,
    val targetLanguage: TranslateLanguageOption,
    val sourceText: String,
    val translatedText: String,
)

object MapTranslateHistoryItemUseCase {
    operator fun invoke(item: TranslationHistoryEntity): TranslateHistoryPrefill? {
        return when (item.mode) {
            HistoryMode.TEXT -> TranslateHistoryPrefill(
                isTextMode = true,
                sourceLanguage = translateLanguageFromStored(item.sourceLanguage),
                targetLanguage = translateLanguageFromStored(item.targetLanguage),
                detectedSourceLanguage = translateLanguageFromStored(item.sourceLanguage),
                textInput = item.sourceText,
                textTranslation = item.translatedText,
                recognizedLines = emptyList(),
                translatedLines = emptyList()
            )

            HistoryMode.OCR -> TranslateHistoryPrefill(
                isTextMode = false,
                sourceLanguage = translateLanguageFromStored(item.sourceLanguage),
                targetLanguage = translateLanguageFromStored(item.targetLanguage),
                detectedSourceLanguage = null,
                textInput = "",
                textTranslation = "",
                recognizedLines = item.sourceText.split('\n').map { it.trim() }.filter { it.isNotEmpty() },
                translatedLines = item.translatedText.split('\n').map { it.trim() }.filter { it.isNotEmpty() }
            )

            HistoryMode.VOICE -> null
        }
    }
}

object MapVoiceHistoryItemUseCase {
    operator fun invoke(item: TranslationHistoryEntity): VoiceHistoryPrefill? {
        if (item.mode != HistoryMode.VOICE) return null
        return VoiceHistoryPrefill(
            sourceLanguage = translateLanguageFromStored(item.sourceLanguage),
            targetLanguage = translateLanguageFromStored(item.targetLanguage),
            sourceText = item.sourceText,
            translatedText = item.translatedText
        )
    }
}
