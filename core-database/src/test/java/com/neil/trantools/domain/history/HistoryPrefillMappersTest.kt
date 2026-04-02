package com.neil.trantools.domain.history

import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.feature.translate.TranslateLanguageOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HistoryPrefillMappersTest {
    @Test
    fun `map text history into text prefill`() {
        val prefill = MapTranslateHistoryItemUseCase(
            TranslationHistoryEntity(
                mode = HistoryMode.TEXT,
                sourceLanguage = "en",
                targetLanguage = "ja",
                sourceText = "Where is the station?",
                translatedText = "駅はどこですか？"
            )
        )

        requireNotNull(prefill)
        assertTrue(prefill.isTextMode)
        assertEquals(TranslateLanguageOption.English, prefill.sourceLanguage)
        assertEquals(TranslateLanguageOption.Japanese, prefill.targetLanguage)
        assertEquals("Where is the station?", prefill.textInput)
    }

    @Test
    fun `map ocr history into camera prefill`() {
        val prefill = MapTranslateHistoryItemUseCase(
            TranslationHistoryEntity(
                mode = HistoryMode.OCR,
                sourceLanguage = "ja",
                targetLanguage = "en",
                sourceText = "焼き鮭\n味噌汁",
                translatedText = "Grilled salmon\nMiso soup"
            )
        )

        requireNotNull(prefill)
        assertFalse(prefill.isTextMode)
        assertEquals(listOf("焼き鮭", "味噌汁"), prefill.recognizedLines)
        assertEquals(listOf("Grilled salmon", "Miso soup"), prefill.translatedLines)
    }

    @Test
    fun `ignore voice item for translate prefill`() {
        val prefill = MapTranslateHistoryItemUseCase(
            TranslationHistoryEntity(
                mode = HistoryMode.VOICE,
                sourceLanguage = "en",
                targetLanguage = "ja",
                sourceText = "Hello",
                translatedText = "こんにちは"
            )
        )

        assertNull(prefill)
    }

    @Test
    fun `map voice history into voice prefill`() {
        val prefill = MapVoiceHistoryItemUseCase(
            TranslationHistoryEntity(
                mode = HistoryMode.VOICE,
                sourceLanguage = "en",
                targetLanguage = "ja",
                sourceText = "Hello",
                translatedText = "こんにちは"
            )
        )

        requireNotNull(prefill)
        assertEquals(TranslateLanguageOption.English, prefill.sourceLanguage)
        assertEquals(TranslateLanguageOption.Japanese, prefill.targetLanguage)
        assertEquals("Hello", prefill.sourceText)
    }
}
