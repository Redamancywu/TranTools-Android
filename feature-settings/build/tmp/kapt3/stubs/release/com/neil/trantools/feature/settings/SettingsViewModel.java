package com.neil.trantools.feature.settings;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.neil.trantools.core.resource.ThemeStyleStore;
import com.neil.trantools.data.settings.BehaviorPreferencesStore;
import com.neil.trantools.data.settings.LocalSubscriptionState;
import com.neil.trantools.data.settings.ResourcePackageRepository;
import com.neil.trantools.data.settings.SubscriptionStore;
import com.neil.trantools.data.translation.TranslationModelStore;
import com.neil.trantools.ui.theme.AppThemeStyle;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import java.io.File;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u0010J\u000e\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001cR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001d"}, d2 = {"Lcom/neil/trantools/feature/settings/SettingsViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/neil/trantools/feature/settings/SettingsUiState;", "appContext", "Landroid/content/Context;", "kotlin.jvm.PlatformType", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearCache", "", "refreshResources", "setAutoDownloadOnWifi", "value", "", "setHighQualityOcr", "setOfflineOnlyMode", "setSubscriptionState", "state", "Lcom/neil/trantools/data/settings/LocalSubscriptionState;", "setThemeStyle", "style", "Lcom/neil/trantools/ui/theme/AppThemeStyle;", "feature-settings_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.AndroidViewModel {
    private final android.content.Context appContext = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.neil.trantools.feature.settings.SettingsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.settings.SettingsUiState> uiState = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.settings.SettingsUiState> getUiState() {
        return null;
    }
    
    public final void refreshResources() {
    }
    
    public final void setThemeStyle(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.ui.theme.AppThemeStyle style) {
    }
    
    public final void setSubscriptionState(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.settings.LocalSubscriptionState state) {
    }
    
    public final void setOfflineOnlyMode(boolean value) {
    }
    
    public final void setAutoDownloadOnWifi(boolean value) {
    }
    
    public final void setHighQualityOcr(boolean value) {
    }
    
    public final void clearCache() {
    }
}