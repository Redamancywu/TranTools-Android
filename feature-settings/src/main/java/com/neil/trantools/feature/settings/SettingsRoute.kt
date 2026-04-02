package com.neil.trantools.feature.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neil.trantools.data.settings.LocalSubscriptionState
import com.neil.trantools.data.translation.TranslationModelStore
import kotlinx.coroutines.launch

@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    SettingsScreen(
        modifier = modifier,
        selectedStyle = uiState.themeStyle,
        translationPackStatuses = uiState.translationPackStatuses,
        modelPacks = uiState.modelPacks,
        cityPacks = uiState.cityPacks,
        storageSummary = uiState.storageSummary,
        onStyleSelected = viewModel::setThemeStyle,
        onDownloadLanguagePack = { language ->
            scope.launch {
                TranslationModelStore.download(language)
            }
        },
        onDeleteLanguagePack = { language ->
            scope.launch {
                TranslationModelStore.delete(language)
            }
        },
        onRefreshResources = viewModel::refreshResources,
        onBack = onBack,
        subscriptionState = if (uiState.subscriptionState == LocalSubscriptionState.Pro) {
            SubscriptionState.Pro
        } else {
            SubscriptionState.Free
        },
        onSubscribeClick = { viewModel.setSubscriptionState(LocalSubscriptionState.Pro) },
        onRestorePurchaseClick = { viewModel.setSubscriptionState(LocalSubscriptionState.Pro) },
        onManageSubscriptionClick = { viewModel.setSubscriptionState(LocalSubscriptionState.Free) },
        onInstallModelPack = viewModel::installModelPack,
        onRemoveModelPack = viewModel::removeModelPack,
        onDownloadCityPack = viewModel::downloadCityPack,
        onDeleteCityPack = viewModel::deleteCityPack,
        onOpenLanguagePacks = viewModel::refreshResources,
        onOpenModelPacks = viewModel::refreshResources,
        onOpenStorageManager = viewModel::refreshResources,
        onOpenPrivacy = {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://example.com/trantools/privacy")
                )
            )
        },
        onOpenSupport = {
            context.startActivity(
                Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("mailto:support@trantools.local")
                ).apply {
                    putExtra(Intent.EXTRA_SUBJECT, "TranTools Feedback")
                }
            )
        },
        onClearCache = viewModel::clearCache,
        offlineOnlyMode = uiState.offlineOnlyMode,
        autoDownloadOnWifi = uiState.autoDownloadOnWifi,
        highQualityOcr = uiState.highQualityOcr,
        onOfflineOnlyModeChange = viewModel::setOfflineOnlyMode,
        onAutoDownloadOnWifiChange = viewModel::setAutoDownloadOnWifi,
        onHighQualityOcrChange = viewModel::setHighQualityOcr
    )
}
