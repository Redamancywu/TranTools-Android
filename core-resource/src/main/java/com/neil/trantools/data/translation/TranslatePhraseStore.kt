package com.neil.trantools.data.translation

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.neil.trantools.feature.translate.TranslateLanguageOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.translatePhraseStore by preferencesDataStore(name = "translate_phrase_store")
private val favoritePhraseEntriesKey = stringSetPreferencesKey("favorite_phrase_entries")

data class FavoritePhrase(
    val sourceLanguage: String,
    val targetLanguage: String,
    val sourceText: String,
    val translatedText: String,
)

object TranslatePhraseStore {
    private const val separator = "||"

    fun observeFavorites(context: Context): Flow<List<FavoritePhrase>> {
        return context.translatePhraseStore.data.map { prefs ->
            prefs[favoritePhraseEntriesKey]
                .orEmpty()
                .mapNotNull(::decode)
                .sortedBy { it.sourceText.lowercase() }
        }
    }

    suspend fun toggleFavorite(
        context: Context,
        phrase: FavoritePhrase,
    ) {
        context.translatePhraseStore.edit { prefs ->
            val current = prefs[favoritePhraseEntriesKey].orEmpty().toMutableSet()
            val encoded = encode(phrase)
            if (!current.add(encoded)) {
                current.remove(encoded)
            }
            prefs[favoritePhraseEntriesKey] = current
        }
    }

    fun defaultCommonPhrases(): List<FavoritePhrase> {
        return listOf(
            FavoritePhrase("en", "ja", "Where is the nearest station?", "最寄り駅はどこですか？"),
            FavoritePhrase("en", "zh", "How much does this cost?", "这个多少钱？"),
            FavoritePhrase("en", "es", "Do you have a vegetarian option?", "¿Tiene una opción vegetariana?"),
            FavoritePhrase("ja", "en", "おすすめは何ですか？", "What do you recommend?"),
            FavoritePhrase("zh", "en", "可以帮我拍张照片吗？", "Could you help me take a photo?")
        )
    }

    private fun encode(phrase: FavoritePhrase): String {
        return listOf(
            phrase.sourceLanguage,
            phrase.targetLanguage,
            phrase.sourceText,
            phrase.translatedText
        ).joinToString(separator)
    }

    private fun decode(raw: String): FavoritePhrase? {
        val parts = raw.split(separator)
        if (parts.size != 4) return null
        return FavoritePhrase(
            sourceLanguage = parts[0],
            targetLanguage = parts[1],
            sourceText = parts[2],
            translatedText = parts[3]
        )
    }
}
