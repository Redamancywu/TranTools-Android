package com.neil.trantools.feature.settings

import com.neil.trantools.data.settings.CityPackInfo
import com.neil.trantools.data.settings.ModelPackInfo
import com.neil.trantools.data.settings.StorageSummary
import com.neil.trantools.data.settings.LocalSubscriptionState
import com.neil.trantools.data.translation.TranslationPackStatus
import com.neil.trantools.ui.theme.AppThemeStyle

data class SettingsUiState(
    val themeStyle: AppThemeStyle = AppThemeStyle.Mint,
    val translationPackStatuses: List<TranslationPackStatus> = emptyList(),
    val modelPacks: List<ModelPackInfo> = emptyList(),
    val cityPacks: List<CityPackInfo> = emptyList(),
    val storageSummary: StorageSummary = StorageSummary(0, 0, 0, 0),
    val subscriptionState: LocalSubscriptionState = LocalSubscriptionState.Free,
    val offlineOnlyMode: Boolean = true,
    val autoDownloadOnWifi: Boolean = true,
    val highQualityOcr: Boolean = false,
)
