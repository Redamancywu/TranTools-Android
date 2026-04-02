package com.neil.trantools.feature.settings;

import com.neil.trantools.data.settings.CityPackInfo;
import com.neil.trantools.data.settings.ModelPackInfo;
import com.neil.trantools.data.settings.StorageSummary;
import com.neil.trantools.data.settings.LocalSubscriptionState;
import com.neil.trantools.data.translation.TranslationPackStatus;
import com.neil.trantools.ui.theme.AppThemeStyle;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001e\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001Bq\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0010\u00a2\u0006\u0002\u0010\u0013J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u0010$\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\u000f\u0010%\u001a\b\u0012\u0004\u0012\u00020\n0\u0005H\u00c6\u0003J\t\u0010&\u001a\u00020\fH\u00c6\u0003J\t\u0010\'\u001a\u00020\u000eH\u00c6\u0003J\t\u0010(\u001a\u00020\u0010H\u00c6\u0003J\t\u0010)\u001a\u00020\u0010H\u00c6\u0003J\t\u0010*\u001a\u00020\u0010H\u00c6\u0003Ju\u0010+\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00052\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u0010H\u00c6\u0001J\u0013\u0010,\u001a\u00020\u00102\b\u0010-\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010.\u001a\u00020/H\u00d6\u0001J\t\u00100\u001a\u000201H\u00d6\u0001R\u0011\u0010\u0011\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0012\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0015R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0015R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0017\u00a8\u00062"}, d2 = {"Lcom/neil/trantools/feature/settings/SettingsUiState;", "", "themeStyle", "Lcom/neil/trantools/ui/theme/AppThemeStyle;", "translationPackStatuses", "", "Lcom/neil/trantools/data/translation/TranslationPackStatus;", "modelPacks", "Lcom/neil/trantools/data/settings/ModelPackInfo;", "cityPacks", "Lcom/neil/trantools/data/settings/CityPackInfo;", "storageSummary", "Lcom/neil/trantools/data/settings/StorageSummary;", "subscriptionState", "Lcom/neil/trantools/data/settings/LocalSubscriptionState;", "offlineOnlyMode", "", "autoDownloadOnWifi", "highQualityOcr", "(Lcom/neil/trantools/ui/theme/AppThemeStyle;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lcom/neil/trantools/data/settings/StorageSummary;Lcom/neil/trantools/data/settings/LocalSubscriptionState;ZZZ)V", "getAutoDownloadOnWifi", "()Z", "getCityPacks", "()Ljava/util/List;", "getHighQualityOcr", "getModelPacks", "getOfflineOnlyMode", "getStorageSummary", "()Lcom/neil/trantools/data/settings/StorageSummary;", "getSubscriptionState", "()Lcom/neil/trantools/data/settings/LocalSubscriptionState;", "getThemeStyle", "()Lcom/neil/trantools/ui/theme/AppThemeStyle;", "getTranslationPackStatuses", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "", "feature-settings_release"})
public final class SettingsUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.ui.theme.AppThemeStyle themeStyle = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.data.translation.TranslationPackStatus> translationPackStatuses = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.data.settings.ModelPackInfo> modelPacks = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.data.settings.CityPackInfo> cityPacks = null;
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.data.settings.StorageSummary storageSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.data.settings.LocalSubscriptionState subscriptionState = null;
    private final boolean offlineOnlyMode = false;
    private final boolean autoDownloadOnWifi = false;
    private final boolean highQualityOcr = false;
    
    public SettingsUiState(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.ui.theme.AppThemeStyle themeStyle, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.translation.TranslationPackStatus> translationPackStatuses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.settings.ModelPackInfo> modelPacks, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.settings.CityPackInfo> cityPacks, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.settings.StorageSummary storageSummary, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.settings.LocalSubscriptionState subscriptionState, boolean offlineOnlyMode, boolean autoDownloadOnWifi, boolean highQualityOcr) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.ui.theme.AppThemeStyle getThemeStyle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.translation.TranslationPackStatus> getTranslationPackStatuses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.settings.ModelPackInfo> getModelPacks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.settings.CityPackInfo> getCityPacks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.data.settings.StorageSummary getStorageSummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.data.settings.LocalSubscriptionState getSubscriptionState() {
        return null;
    }
    
    public final boolean getOfflineOnlyMode() {
        return false;
    }
    
    public final boolean getAutoDownloadOnWifi() {
        return false;
    }
    
    public final boolean getHighQualityOcr() {
        return false;
    }
    
    public SettingsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.ui.theme.AppThemeStyle component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.translation.TranslationPackStatus> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.settings.ModelPackInfo> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.settings.CityPackInfo> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.data.settings.StorageSummary component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.data.settings.LocalSubscriptionState component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.settings.SettingsUiState copy(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.ui.theme.AppThemeStyle themeStyle, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.translation.TranslationPackStatus> translationPackStatuses, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.settings.ModelPackInfo> modelPacks, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.settings.CityPackInfo> cityPacks, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.settings.StorageSummary storageSummary, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.settings.LocalSubscriptionState subscriptionState, boolean offlineOnlyMode, boolean autoDownloadOnWifi, boolean highQualityOcr) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}