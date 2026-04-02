package com.neil.trantools.feature.translate

enum class TranslateLanguageOption(
    val code: String,
    val legacyDisplayName: String,
) {
    Auto("auto", "Auto"),
    English("en", "English"),
    Japanese("ja", "Japanese"),
    Chinese("zh", "Chinese"),
    Korean("ko", "Korean"),
    Spanish("es", "Spanish"),
}

val supportedTranslateLanguages = TranslateLanguageOption.entries.filter { it != TranslateLanguageOption.Auto }

fun TranslateLanguageOption.toMlKitCode(): String? = code.takeUnless { this == TranslateLanguageOption.Auto }

fun translateLanguageFromStored(raw: String): TranslateLanguageOption {
    return TranslateLanguageOption.entries.firstOrNull {
        it.code.equals(raw, ignoreCase = true) ||
            it.legacyDisplayName.equals(raw, ignoreCase = true)
    } ?: TranslateLanguageOption.English
}

fun detectTranslateLanguage(text: String): TranslateLanguageOption? {
    val content = text.trim()
    if (content.isEmpty()) return null

    val hasKana = content.any { it in '\u3040'..'\u30ff' }
    if (hasKana) return TranslateLanguageOption.Japanese

    val hasHangul = content.any { it in '\uac00'..'\ud7af' || it in '\u1100'..'\u11ff' }
    if (hasHangul) return TranslateLanguageOption.Korean

    val hasCjk = content.any { it in '\u4e00'..'\u9fff' }
    if (hasCjk) return TranslateLanguageOption.Chinese

    val normalized = content.lowercase()
    val spanishHints = listOf(
        " el ", " la ", " de ", " que ", " por ", " para ", " con ", " sin ", " gracias ",
        " donde ", " estación", " menu", " desayuno", " cena", " por favor"
    )
    val hasSpanishPunctuation = normalized.contains('¿') || normalized.contains('¡')
    val hasSpanishAccent = normalized.any { it in "áéíóúñü" }
    if (hasSpanishPunctuation || hasSpanishAccent || spanishHints.any { normalized.contains(it) }) {
        return TranslateLanguageOption.Spanish
    }

    val hasLatin = normalized.any { it in 'a'..'z' }
    if (hasLatin) return TranslateLanguageOption.English

    return null
}
