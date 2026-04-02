package com.neil.trantools.data.translation

import com.neil.trantools.feature.translate.TranslateLanguageOption

fun TranslationPackStatus.matches(language: TranslateLanguageOption): Boolean = this.language == language
