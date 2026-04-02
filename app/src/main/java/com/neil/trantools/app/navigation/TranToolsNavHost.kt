package com.neil.trantools.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.feature.chat.ChatRoute
import com.neil.trantools.feature.gems.GemDetailRoute
import com.neil.trantools.feature.gems.GemsRoute
import com.neil.trantools.feature.history.HistoryRoute
import com.neil.trantools.feature.home.HomeRoute
import com.neil.trantools.feature.settings.SettingsRoute
import com.neil.trantools.feature.translate.TranslateRoute
import com.neil.trantools.feature.voice.VoiceRoute
import com.neil.trantools.feature.wiki.WikiDetailRoute
import com.neil.trantools.feature.wiki.WikiRoute

private const val settingsRoute = "settings"
private const val historyRoute = "history"
private const val wikiDetailRoute = "wiki_detail"
private const val gemDetailRoute = "gem_detail"
private const val chatRoute = "chat"

@Composable
fun TranToolsNavHost(
    appState: TranToolsAppState,
    modifier: Modifier = Modifier,
) {
    var selectedTranslateHistoryItem by remember { mutableStateOf<TranslationHistoryEntity?>(null) }
    var selectedVoiceHistoryItem by remember { mutableStateOf<TranslationHistoryEntity?>(null) }
    var pendingWikiQuestion by remember { mutableStateOf<String?>(null) }
    var pendingChatQuestion by remember { mutableStateOf<String?>(null) }

    NavHost(
        navController = appState.navController,
        startDestination = TopLevelDestination.Home.route,
        modifier = modifier
    ) {
        composable(route = TopLevelDestination.Home.route) {
            HomeRoute(
                onOpenSettings = { appState.navController.navigate(settingsRoute) },
                onOpenChat = { appState.navController.navigate(chatRoute) },
                onOpenTranslate = { appState.navigateToTopLevel(TopLevelDestination.Translate.route) },
                onOpenVoice = { appState.navigateToTopLevel(TopLevelDestination.Voice.route) },
                onOpenRecentHistory = { item ->
                    when (item.mode) {
                        HistoryMode.TEXT,
                        HistoryMode.OCR -> {
                            selectedTranslateHistoryItem = item
                            selectedVoiceHistoryItem = null
                            appState.navigateToTopLevel(TopLevelDestination.Translate.route)
                        }

                        HistoryMode.VOICE -> {
                            selectedVoiceHistoryItem = item
                            selectedTranslateHistoryItem = null
                            appState.navigateToTopLevel(TopLevelDestination.Voice.route)
                        }
                    }
                }
            )
        }

        composable(route = chatRoute) {
            ChatRoute(
                onBack = { appState.navController.popBackStack() },
                onOpenSettings = { appState.navController.navigate(settingsRoute) },
                prefillQuestion = pendingChatQuestion,
                onPrefillConsumed = { pendingChatQuestion = null },
                onOpenWikiArticle = { articleId ->
                    appState.navController.navigate("$wikiDetailRoute/$articleId")
                },
                onOpenGemDetail = { poiId ->
                    appState.navController.navigate("$gemDetailRoute/$poiId")
                }
            )
        }

        composable(route = TopLevelDestination.Translate.route) {
            TranslateRoute(
                onOpenSettings = { appState.navController.navigate(settingsRoute) },
                onOpenHistory = { appState.navController.navigate(historyRoute) },
                prefillHistoryItem = selectedTranslateHistoryItem,
                onPrefillConsumed = { selectedTranslateHistoryItem = null }
            )
        }

        composable(route = TopLevelDestination.Voice.route) {
            VoiceRoute(
                onOpenSettings = { appState.navController.navigate(settingsRoute) },
                onOpenHistory = { appState.navController.navigate(historyRoute) },
                prefillHistoryItem = selectedVoiceHistoryItem,
                onPrefillConsumed = { selectedVoiceHistoryItem = null }
            )
        }

        composable(route = TopLevelDestination.Wiki.route) {
            WikiRoute(
                onOpenSettings = { appState.navController.navigate(settingsRoute) },
                prefillQuestion = pendingWikiQuestion,
                onPrefillConsumed = { pendingWikiQuestion = null },
                onOpenChat = { question ->
                    pendingChatQuestion = question
                    appState.navController.navigate(chatRoute) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onOpenArticle = { articleId ->
                    appState.navController.navigate("$wikiDetailRoute/$articleId")
                }
            )
        }

        composable(route = "$wikiDetailRoute/{articleId}") { backStackEntry ->
            WikiDetailRoute(
                articleId = backStackEntry.arguments?.getString("articleId").orEmpty(),
                onBack = { appState.navController.popBackStack() },
                onOpenSettings = { appState.navController.navigate(settingsRoute) }
            )
        }

        composable(route = TopLevelDestination.Gems.route) {
            GemsRoute(
                onOpenSettings = { appState.navController.navigate(settingsRoute) },
                onOpenDetail = { poiId ->
                    appState.navController.navigate("$gemDetailRoute/$poiId")
                }
            )
        }

        composable(route = "$gemDetailRoute/{poiId}") { backStackEntry ->
            GemDetailRoute(
                poiId = backStackEntry.arguments?.getString("poiId").orEmpty(),
                onBack = { appState.navController.popBackStack() },
                onOpenSettings = { appState.navController.navigate(settingsRoute) },
                onOpenWikiArticle = { articleId ->
                    appState.navController.navigate("$wikiDetailRoute/$articleId")
                },
                onOpenWikiQuestion = { question ->
                    pendingWikiQuestion = question
                    appState.navController.navigate(TopLevelDestination.Wiki.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onOpenChatQuestion = { question ->
                    pendingChatQuestion = question
                    appState.navController.navigate(chatRoute) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(route = settingsRoute) {
            SettingsRoute(
                onBack = { appState.navController.popBackStack() }
            )
        }

        composable(route = historyRoute) {
            HistoryRoute(
                onUseItem = { item ->
                    when (item.mode) {
                        HistoryMode.TEXT,
                        HistoryMode.OCR -> {
                            selectedTranslateHistoryItem = item
                            selectedVoiceHistoryItem = null
                            appState.navigateToTopLevel(TopLevelDestination.Translate.route)
                        }

                        HistoryMode.VOICE -> {
                            selectedVoiceHistoryItem = item
                            selectedTranslateHistoryItem = null
                            appState.navigateToTopLevel(TopLevelDestination.Voice.route)
                        }
                    }
                },
                onBack = { appState.navController.popBackStack() }
            )
        }
    }
}
