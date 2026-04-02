package com.neil.trantools.feature.settings;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.tooling.preview.Preview;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.settings.CityPackInfo;
import com.neil.trantools.data.settings.ModelPackInfo;
import com.neil.trantools.data.settings.StorageSummary;
import com.neil.trantools.data.translation.TranslationPackStatus;
import com.neil.trantools.feature.translate.TranslateLanguageOption;
import com.neil.trantools.ui.theme.AppThemeStyle;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000n\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\t\u001a.\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u000bH\u0003\u001a\u001e\u0010\f\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u0001H\u0003\u001a\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\bH\u0003\u001a\u00aa\u0003\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u00012\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00012\b\b\u0002\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00040\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\b\b\u0002\u0010\u001d\u001a\u00020\u001e2\b\b\u0002\u0010\u001f\u001a\u00020 2\u0014\b\u0002\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00040\u001b2\u0014\b\u0002\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00040\u001b2\u000e\b\u0002\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\u000e\b\u0002\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\b\b\u0002\u0010.\u001a\u00020/2\b\b\u0002\u00100\u001a\u00020/2\b\b\u0002\u00101\u001a\u00020/2\u0014\b\u0002\u00102\u001a\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u00040\u001b2\u0014\b\u0002\u00103\u001a\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u00040\u001b2\u0014\b\u0002\u00104\u001a\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u00040\u001bH\u0007\u001a\b\u00105\u001a\u00020\u0004H\u0003\u001a\b\u00106\u001a\u00020\u0004H\u0003\u001a:\u00107\u001a\u00020\u00042\u0006\u00108\u001a\u00020 2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00040\u000bH\u0003\u001a<\u00109\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010:\u001a\u00020\b2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020<0\u00012\u0006\u0010=\u001a\u00020/2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u000bH\u0003\u001a<\u0010>\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010?\u001a\u00020/2\u0012\u0010@\u001a\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u00040\u001bH\u0003\u001a,\u0010A\u001a\u00020\u00042\u0006\u0010B\u001a\u00020\u00022\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00040\u000b2\f\u0010D\u001a\b\u0012\u0004\u0012\u00020\u00040\u000bH\u0003\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006E"}, d2 = {"previewTranslationStatuses", "", "Lcom/neil/trantools/data/translation/TranslationPackStatus;", "ActionRow", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "title", "", "subtitle", "onClick", "Lkotlin/Function0;", "InfoListCard", "rows", "SectionTitle", "text", "SettingsScreen", "selectedStyle", "Lcom/neil/trantools/ui/theme/AppThemeStyle;", "translationPackStatuses", "modelPacks", "Lcom/neil/trantools/data/settings/ModelPackInfo;", "cityPacks", "Lcom/neil/trantools/data/settings/CityPackInfo;", "storageSummary", "Lcom/neil/trantools/data/settings/StorageSummary;", "onStyleSelected", "Lkotlin/Function1;", "onBack", "modifier", "Landroidx/compose/ui/Modifier;", "subscriptionState", "Lcom/neil/trantools/feature/settings/SubscriptionState;", "onDownloadLanguagePack", "Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "onDeleteLanguagePack", "onRefreshResources", "onSubscribeClick", "onRestorePurchaseClick", "onManageSubscriptionClick", "onOpenLanguagePacks", "onOpenModelPacks", "onOpenStorageManager", "onClearCache", "onOpenPrivacy", "onOpenSupport", "offlineOnlyMode", "", "autoDownloadOnWifi", "highQualityOcr", "onOfflineOnlyModeChange", "onAutoDownloadOnWifiChange", "onHighQualityOcrChange", "SettingsScreenPreviewMintFree", "SettingsScreenPreviewWarmPro", "SubscriptionCard", "state", "ThemeOptionCard", "description", "swatches", "Landroidx/compose/ui/graphics/Color;", "selected", "ToggleRow", "checked", "onCheckedChange", "TranslationPackRow", "status", "onDownload", "onDelete", "feature-settings_debug"})
public final class SettingsScreenKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.neil.trantools.data.translation.TranslationPackStatus> previewTranslationStatuses = null;
    
    @androidx.compose.runtime.Composable()
    public static final void SettingsScreen(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.ui.theme.AppThemeStyle selectedStyle, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.translation.TranslationPackStatus> translationPackStatuses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.settings.ModelPackInfo> modelPacks, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.settings.CityPackInfo> cityPacks, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.settings.StorageSummary storageSummary, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.ui.theme.AppThemeStyle, kotlin.Unit> onStyleSelected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.settings.SubscriptionState subscriptionState, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.feature.translate.TranslateLanguageOption, kotlin.Unit> onDownloadLanguagePack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.feature.translate.TranslateLanguageOption, kotlin.Unit> onDeleteLanguagePack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRefreshResources, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSubscribeClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRestorePurchaseClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onManageSubscriptionClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenLanguagePacks, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenModelPacks, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenStorageManager, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClearCache, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenPrivacy, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSupport, boolean offlineOnlyMode, boolean autoDownloadOnWifi, boolean highQualityOcr, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onOfflineOnlyModeChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onAutoDownloadOnWifiChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onHighQualityOcrChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void InfoListCard(java.lang.String title, java.util.List<java.lang.String> rows) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TranslationPackRow(com.neil.trantools.data.translation.TranslationPackStatus status, kotlin.jvm.functions.Function0<kotlin.Unit> onDownload, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SubscriptionCard(com.neil.trantools.feature.settings.SubscriptionState state, kotlin.jvm.functions.Function0<kotlin.Unit> onSubscribeClick, kotlin.jvm.functions.Function0<kotlin.Unit> onRestorePurchaseClick, kotlin.jvm.functions.Function0<kotlin.Unit> onManageSubscriptionClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionTitle(java.lang.String text) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ThemeOptionCard(java.lang.String title, java.lang.String description, java.util.List<androidx.compose.ui.graphics.Color> swatches, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ActionRow(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String subtitle, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ToggleRow(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String subtitle, boolean checked, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onCheckedChange) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable()
    private static final void SettingsScreenPreviewMintFree() {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable()
    private static final void SettingsScreenPreviewWarmPro() {
    }
}