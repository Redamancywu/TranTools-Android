package com.neil.trantools.feature.home

import androidx.compose.ui.graphics.vector.ImageVector
import com.neil.trantools.data.history.TranslationHistoryEntity

data class HomeUiState(
    val title: String = "",
    val subtitle: String = "",
    val quickActions: List<HomeQuickAction> = emptyList(),
    val recentItems: List<String> = emptyList(),
    val statusItems: List<String> = emptyList(),
    val recentHistory: List<TranslationHistoryEntity> = emptyList(),
)

data class HomeQuickAction(
    val title: String,
    val description: String,
    val icon: ImageVector,
)
