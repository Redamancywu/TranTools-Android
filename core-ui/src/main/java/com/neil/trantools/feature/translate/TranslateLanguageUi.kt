package com.neil.trantools.feature.translate

import android.content.Context
import com.neil.trantools.core.ui.R

fun TranslateLanguageOption.displayLabel(context: Context): String {
    return when (this) {
        TranslateLanguageOption.Auto -> context.getString(R.string.language_auto)
        TranslateLanguageOption.English -> context.getString(R.string.language_english)
        TranslateLanguageOption.Japanese -> context.getString(R.string.language_japanese)
        TranslateLanguageOption.Chinese -> context.getString(R.string.language_chinese)
        TranslateLanguageOption.Korean -> context.getString(R.string.language_korean)
        TranslateLanguageOption.Spanish -> context.getString(R.string.language_spanish)
    }
}
