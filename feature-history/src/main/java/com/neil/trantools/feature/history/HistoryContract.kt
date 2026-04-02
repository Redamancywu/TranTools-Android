package com.neil.trantools.feature.history

import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.TranslationHistoryEntity

enum class HistoryFilter {
    All,
    Text,
    Ocr,
    Voice
}

data class HistoryUiState(
    val filter: HistoryFilter = HistoryFilter.All,
    val items: List<TranslationHistoryEntity> = emptyList(),
)

fun HistoryUiState.filteredItems(): List<TranslationHistoryEntity> {
    return when (filter) {
        HistoryFilter.All -> items
        HistoryFilter.Text -> items.filter { it.mode == HistoryMode.TEXT }
        HistoryFilter.Ocr -> items.filter { it.mode == HistoryMode.OCR }
        HistoryFilter.Voice -> items.filter { it.mode == HistoryMode.VOICE }
    }
}
