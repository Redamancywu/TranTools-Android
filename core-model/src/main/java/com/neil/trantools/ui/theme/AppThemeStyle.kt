package com.neil.trantools.ui.theme

enum class AppThemeStyle(val key: String) {
    Mint("mint"),
    Warm("warm");

    companion object {
        fun fromKey(raw: String?): AppThemeStyle {
            return entries.firstOrNull { it.key == raw } ?: Mint
        }
    }
}
