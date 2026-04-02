package com.neil.trantools.feature.translate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TranslateLanguageTest {
    @Test
    fun `detect japanese when kana is present`() {
        assertEquals(TranslateLanguageOption.Japanese, detectTranslateLanguage("おはようございます"))
    }

    @Test
    fun `detect korean when hangul is present`() {
        assertEquals(TranslateLanguageOption.Korean, detectTranslateLanguage("안녕하세요"))
    }

    @Test
    fun `detect chinese when cjk text without kana is present`() {
        assertEquals(TranslateLanguageOption.Chinese, detectTranslateLanguage("最近的地铁站在哪里"))
    }

    @Test
    fun `detect spanish from punctuation and accents`() {
        assertEquals(TranslateLanguageOption.Spanish, detectTranslateLanguage("¿Dónde está la estación más cercana?"))
    }

    @Test
    fun `detect english from latin text`() {
        assertEquals(TranslateLanguageOption.English, detectTranslateLanguage("Where is the nearest station"))
    }

    @Test
    fun `return null for blank input`() {
        assertNull(detectTranslateLanguage("   "))
    }

    @Test
    fun `map stored code and legacy label`() {
        assertEquals(TranslateLanguageOption.Japanese, translateLanguageFromStored("ja"))
        assertEquals(TranslateLanguageOption.English, translateLanguageFromStored("English"))
    }
}
