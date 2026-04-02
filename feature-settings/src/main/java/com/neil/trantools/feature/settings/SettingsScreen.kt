package com.neil.trantools.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.settings.CityPackInfo
import com.neil.trantools.data.settings.ModelPackInfo
import com.neil.trantools.data.settings.ModelPackInstallStatus
import com.neil.trantools.data.settings.StorageSummary
import com.neil.trantools.data.translation.TranslationPackStatus
import com.neil.trantools.feature.translate.TranslateLanguageOption
import com.neil.trantools.feature.translate.displayLabel
import com.neil.trantools.ui.theme.AppThemeStyle
import com.neil.trantools.ui.theme.TranToolsTheme

enum class SubscriptionState {
    Free,
    Pro
}

@Composable
fun SettingsScreen(
    selectedStyle: AppThemeStyle,
    translationPackStatuses: List<TranslationPackStatus>,
    modelPacks: List<ModelPackInfo> = emptyList(),
    cityPacks: List<CityPackInfo> = emptyList(),
    storageSummary: StorageSummary = StorageSummary(0, 0, 0, 0),
    onStyleSelected: (AppThemeStyle) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    subscriptionState: SubscriptionState = SubscriptionState.Free,
    onDownloadLanguagePack: (TranslateLanguageOption) -> Unit = {},
    onDeleteLanguagePack: (TranslateLanguageOption) -> Unit = {},
    onRefreshResources: () -> Unit = {},
    onSubscribeClick: () -> Unit = {},
    onRestorePurchaseClick: () -> Unit = {},
    onManageSubscriptionClick: () -> Unit = {},
    onOpenLanguagePacks: () -> Unit = {},
    onOpenModelPacks: () -> Unit = {},
    onOpenStorageManager: () -> Unit = {},
    onInstallModelPack: (String) -> Unit = {},
    onRemoveModelPack: (String) -> Unit = {},
    onDownloadCityPack: (String) -> Unit = {},
    onDeleteCityPack: (String) -> Unit = {},
    onClearCache: () -> Unit = {},
    onOpenPrivacy: () -> Unit = {},
    onOpenSupport: () -> Unit = {},
    offlineOnlyMode: Boolean = true,
    autoDownloadOnWifi: Boolean = true,
    highQualityOcr: Boolean = false,
    onOfflineOnlyModeChange: (Boolean) -> Unit = {},
    onAutoDownloadOnWifiChange: (Boolean) -> Unit = {},
    onHighQualityOcrChange: (Boolean) -> Unit = {},
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.action_back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        item {
            SubscriptionCard(
                state = subscriptionState,
                onSubscribeClick = onSubscribeClick,
                onRestorePurchaseClick = onRestorePurchaseClick,
                onManageSubscriptionClick = onManageSubscriptionClick
            )
        }

        item {
            SectionTitle(stringResource(R.string.settings_visual_style))
        }

        item {
            ThemeOptionCard(
                title = stringResource(R.string.settings_theme_mint),
                description = stringResource(R.string.settings_theme_mint_desc),
                swatches = listOf(Color(0xFF006762), Color(0xFF73F1E7), Color(0xFFF3F8F5)),
                selected = selectedStyle == AppThemeStyle.Mint,
                onClick = { onStyleSelected(AppThemeStyle.Mint) }
            )
        }

        item {
            ThemeOptionCard(
                title = stringResource(R.string.settings_theme_warm),
                description = stringResource(R.string.settings_theme_warm_desc),
                swatches = listOf(Color(0xFF914629), Color(0xFFFE9D79), Color(0xFFFCF6ED)),
                selected = selectedStyle == AppThemeStyle.Warm,
                onClick = { onStyleSelected(AppThemeStyle.Warm) }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(stringResource(R.string.settings_translation_packs))
                TextButton(onClick = onRefreshResources) {
                    Text(stringResource(R.string.action_refresh))
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    translationPackStatuses.forEach { status ->
                        TranslationPackRow(
                            status = status,
                            onDownload = { onDownloadLanguagePack(status.language) },
                            onDelete = { onDeleteLanguagePack(status.language) }
                        )
                    }
                }
            }
        }

        item { SectionTitle(stringResource(R.string.settings_resources)) }
        item {
            ActionRow(
                icon = Icons.Outlined.Memory,
                title = stringResource(R.string.settings_model_packs),
                subtitle = stringResource(R.string.settings_model_packs_desc),
                onClick = onOpenModelPacks
            )
        }
        if (modelPacks.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        modelPacks.forEach { pack ->
                            ModelPackRow(
                                pack = pack,
                                isPro = subscriptionState == SubscriptionState.Pro,
                                onInstall = { onInstallModelPack(pack.id) },
                                onRemove = { onRemoveModelPack(pack.id) }
                            )
                        }
                    }
                }
            }
        }
        item {
            ActionRow(
                icon = Icons.Outlined.Language,
                title = stringResource(R.string.settings_city_packs),
                subtitle = stringResource(R.string.settings_city_packs_desc),
                onClick = onOpenLanguagePacks
            )
        }
        if (cityPacks.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        cityPacks.forEach { pack ->
                            CityPackRow(
                                pack = pack,
                                onDownload = { onDownloadCityPack(pack.id) },
                                onDelete = { onDeleteCityPack(pack.id) }
                            )
                        }
                    }
                }
            }
        }
        item {
            ActionRow(
                icon = Icons.Outlined.Storage,
                title = stringResource(R.string.settings_storage_manager),
                subtitle = stringResource(R.string.settings_storage_manager_desc),
                onClick = onOpenStorageManager
            )
        }
        item {
            InfoListCard(
                title = stringResource(R.string.settings_storage_manager),
                rows = listOf(
                    stringResource(R.string.settings_storage_used, storageSummary.usedMb),
                    stringResource(
                        R.string.settings_storage_breakdown,
                        storageSummary.modelMb,
                        storageSummary.cityMb,
                        storageSummary.wikiMb
                    )
                )
            )
        }
        item {
            ActionRow(
                icon = Icons.Outlined.CleaningServices,
                title = stringResource(R.string.settings_clear_cache),
                subtitle = stringResource(R.string.settings_clear_cache_desc),
                onClick = onClearCache
            )
        }

        item { SectionTitle(stringResource(R.string.settings_behavior_privacy)) }
        item {
            ToggleRow(
                icon = Icons.Outlined.Lock,
                title = stringResource(R.string.settings_offline_only),
                subtitle = stringResource(R.string.settings_offline_only_desc),
                checked = offlineOnlyMode,
                onCheckedChange = onOfflineOnlyModeChange
            )
        }
        item {
            ToggleRow(
                icon = Icons.Outlined.CloudDownload,
                title = stringResource(R.string.settings_auto_download_wifi),
                subtitle = stringResource(R.string.settings_auto_download_wifi_desc),
                checked = autoDownloadOnWifi,
                onCheckedChange = onAutoDownloadOnWifiChange
            )
        }
        item {
            ToggleRow(
                icon = Icons.Outlined.AutoAwesome,
                title = stringResource(R.string.settings_high_quality_ocr),
                subtitle = stringResource(R.string.settings_high_quality_ocr_desc),
                checked = highQualityOcr,
                onCheckedChange = onHighQualityOcrChange
            )
        }

        item { SectionTitle(stringResource(R.string.settings_support)) }
        item {
            ActionRow(
                icon = Icons.Outlined.Lock,
                title = stringResource(R.string.settings_privacy_policy),
                subtitle = stringResource(R.string.settings_privacy_policy_desc),
                onClick = onOpenPrivacy
            )
        }
        item {
            ActionRow(
                icon = Icons.Outlined.SupportAgent,
                title = stringResource(R.string.settings_help_feedback),
                subtitle = stringResource(R.string.settings_help_feedback_desc),
                onClick = onOpenSupport
            )
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun ModelPackRow(
    pack: ModelPackInfo,
    isPro: Boolean,
    onInstall: () -> Unit,
    onRemove: () -> Unit,
) {
    val premiumLocked = pack.premium && !isPro && !pack.installed

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(pack.title, fontWeight = FontWeight.SemiBold)
            Text(
                text = "${pack.description} · ${stringResource(R.string.settings_pack_size, pack.sizeMb)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            pack.runtimeLabel?.let { runtime ->
                Text(
                    text = runtime,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (pack.premium) {
                Text(
                    text = stringResource(R.string.settings_pack_pro_only),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = when (pack.installStatus) {
                    ModelPackInstallStatus.NotInstalled -> stringResource(R.string.settings_pack_not_installed)
                    ModelPackInstallStatus.Downloading -> stringResource(R.string.settings_pack_progress, pack.progressPercent)
                    ModelPackInstallStatus.Ready -> stringResource(R.string.settings_pack_installed)
                    ModelPackInstallStatus.Failed -> stringResource(R.string.settings_pack_failed)
                },
                color = if (pack.installStatus == ModelPackInstallStatus.Failed) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = MaterialTheme.typography.bodySmall
            )
            pack.errorMessage?.takeIf { it.isNotBlank() }?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (pack.id == "translate-core") {
            Text(
                text = if (pack.installed) {
                    stringResource(R.string.settings_pack_installed)
                } else {
                    stringResource(R.string.settings_pack_not_installed)
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else if (pack.installStatus == ModelPackInstallStatus.Downloading) {
            OutlinedButton(onClick = {}, enabled = false) {
                Text(stringResource(R.string.settings_pack_progress, pack.progressPercent))
            }
        } else if (pack.installed) {
            OutlinedButton(onClick = onRemove) {
                Text(stringResource(R.string.action_remove))
            }
        } else if (premiumLocked) {
            OutlinedButton(
                onClick = {},
                enabled = false
            ) {
                Text(stringResource(R.string.settings_pack_pro_only))
            }
        } else {
            Button(onClick = onInstall) {
                Text(stringResource(R.string.action_download))
            }
        }
    }
}

@Composable
private fun CityPackRow(
    pack: CityPackInfo,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(pack.title, fontWeight = FontWeight.SemiBold)
            Text(
                text = "${pack.description} · ${stringResource(R.string.settings_pack_size, pack.sizeMb)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(R.string.settings_pack_poi_count, pack.poiCount),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (pack.installed) {
            OutlinedButton(onClick = onDelete) {
                Text(stringResource(R.string.action_remove))
            }
        } else {
            Button(onClick = onDownload) {
                Text(stringResource(R.string.action_download))
            }
        }
    }
}

@Composable
private fun InfoListCard(
    title: String,
    rows: List<String>,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            rows.forEach { row ->
                Text(row, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun TranslationPackRow(
    status: TranslationPackStatus,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Language,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = status.language.displayLabel(context),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = when {
                    status.isBusy -> stringResource(R.string.settings_pack_updating)
                    status.isDownloaded -> stringResource(R.string.settings_pack_ready)
                    else -> stringResource(R.string.settings_pack_not_downloaded)
                },
                color = if (status.lastError != null) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = MaterialTheme.typography.bodySmall
            )
            status.lastError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (status.isDownloaded) {
            OutlinedButton(
                onClick = onDelete,
                enabled = !status.isBusy
            ) {
                Text(stringResource(R.string.action_remove))
            }
        } else {
            Button(
                onClick = onDownload,
                enabled = !status.isBusy
            ) {
                Text(if (status.isBusy) stringResource(R.string.settings_pack_loading) else stringResource(R.string.action_download))
            }
        }
    }
}

@Composable
private fun SubscriptionCard(
    state: SubscriptionState,
    onSubscribeClick: () -> Unit,
    onRestorePurchaseClick: () -> Unit,
    onManageSubscriptionClick: () -> Unit,
) {
    val isPro = state == SubscriptionState.Pro

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPro) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            if (isPro) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f) else MaterialTheme.colorScheme.primaryContainer,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (isPro) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = if (isPro) stringResource(R.string.settings_pro_active) else stringResource(R.string.settings_upgrade_pro),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isPro) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Text(
                text = if (isPro) {
                    stringResource(R.string.settings_pro_desc_active)
                } else {
                    stringResource(R.string.settings_pro_desc_free)
                },
                color = if (isPro) {
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            if (isPro) {
                Button(onClick = onManageSubscriptionClick) {
                    Text(stringResource(R.string.action_manage_subscription))
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = onSubscribeClick) {
                        Text(stringResource(R.string.action_subscribe))
                    }
                    OutlinedButton(onClick = onRestorePurchaseClick) {
                        Text(stringResource(R.string.action_restore))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ThemeOptionCard(
    title: String,
    description: String,
    swatches: List<Color>,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    swatches.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(color, CircleShape)
                        )
                    }
                }
            }
            RadioButton(selected = selected, onClick = onClick)
        }
    }
}

@Composable
private fun ActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun ToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Column {
                    Text(title, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreviewMintFree() {
    TranToolsTheme(themeStyle = AppThemeStyle.Mint) {
        SettingsScreen(
            selectedStyle = AppThemeStyle.Mint,
            translationPackStatuses = previewTranslationStatuses,
            onStyleSelected = {},
            onBack = {},
            subscriptionState = SubscriptionState.Free
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreviewWarmPro() {
    TranToolsTheme(themeStyle = AppThemeStyle.Warm) {
        SettingsScreen(
            selectedStyle = AppThemeStyle.Warm,
            translationPackStatuses = previewTranslationStatuses,
            onStyleSelected = {},
            onBack = {},
            subscriptionState = SubscriptionState.Pro
        )
    }
}

private val previewTranslationStatuses = listOf(
    TranslationPackStatus(language = TranslateLanguageOption.English, isDownloaded = true),
    TranslationPackStatus(language = TranslateLanguageOption.Japanese, isDownloaded = true),
    TranslationPackStatus(language = TranslateLanguageOption.Chinese, isDownloaded = false),
    TranslationPackStatus(language = TranslateLanguageOption.Korean, isDownloaded = false),
    TranslationPackStatus(language = TranslateLanguageOption.Spanish, isDownloaded = false)
)
