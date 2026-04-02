package com.neil.trantools.feature.home

import androidx.compose.ui.graphics.vector.ImageVector

data class HomeUiState(
    val title: String = "",
    val subtitle: String = "",
    val quickActions: List<HomeQuickAction> = emptyList(),
    val recentItems: List<String> = emptyList(),
    val statusItems: List<String> = emptyList()
)

data class HomeQuickAction(
    val title: String,
    val description: String,
    val icon: ImageVector,
)
