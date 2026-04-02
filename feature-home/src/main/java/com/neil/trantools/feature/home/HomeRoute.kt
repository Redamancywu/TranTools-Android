package com.neil.trantools.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
    onOpenChat: () -> Unit = {},
    onOpenTranslate: () -> Unit = {},
    onOpenVoice: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        modifier = modifier,
        uiState = uiState,
        onOpenSettings = onOpenSettings,
        onOpenChat = onOpenChat,
        onOpenTranslate = onOpenTranslate,
        onOpenVoice = onOpenVoice
    )
}
