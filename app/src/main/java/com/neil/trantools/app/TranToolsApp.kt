package com.neil.trantools.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.neil.trantools.data.chat.ChatStore
import com.neil.trantools.data.content.LocalContentStore
import com.neil.trantools.data.history.HistoryStore
import com.neil.trantools.data.translation.TranslationModelStore
import com.neil.trantools.data.gems.GemsRepository
import com.neil.trantools.core.resource.ThemeStyleStore
import com.neil.trantools.app.navigation.TranToolsAppState
import com.neil.trantools.app.navigation.TranToolsBottomBar
import com.neil.trantools.app.navigation.TranToolsNavHost
import com.neil.trantools.app.navigation.rememberTranToolsAppState
import com.neil.trantools.data.wiki.WikiRepository
import com.neil.trantools.ui.theme.AppThemeStyle
import com.neil.trantools.ui.theme.TranToolsTheme

@Composable
fun TranToolsApp() {
    val context = LocalContext.current.applicationContext
    val themeStyleFlow = remember(context) { ThemeStyleStore.observe(context) }
    val themeStyle by themeStyleFlow.collectAsState(initial = AppThemeStyle.Mint)
    HistoryStore.init(context)
    ChatStore.init(context)
    LocalContentStore.init(context)

    LaunchedEffect(context) {
        runCatching { WikiRepository.syncIndexIfNeeded(context) }
        runCatching { GemsRepository.syncIndexIfNeeded(context) }
    }

    TranToolsTheme(themeStyle = themeStyle) {
        val appState: TranToolsAppState = rememberTranToolsAppState()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { TranToolsBottomBar(appState) }
        ) { innerPadding ->
            TranToolsNavHost(
                appState = appState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TranToolsAppPreview() {
    TranToolsTheme(themeStyle = AppThemeStyle.Mint) {
        val appState = rememberTranToolsAppState()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { TranToolsBottomBar(appState) }
        ) { innerPadding ->
            TranToolsNavHost(
                appState = appState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}
