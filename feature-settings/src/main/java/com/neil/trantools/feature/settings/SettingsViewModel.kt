package com.neil.trantools.feature.settings

import android.app.Application
import android.app.Activity
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neil.trantools.core.resource.ThemeStyleStore
import com.neil.trantools.data.settings.BehaviorPreferencesStore
import com.neil.trantools.data.settings.LocalSubscriptionState
import com.neil.trantools.data.settings.PlayBillingStore
import com.neil.trantools.data.settings.ResourcePackageRepository
import com.neil.trantools.data.settings.SubscriptionStore
import com.neil.trantools.data.translation.TranslationModelStore
import com.neil.trantools.ui.theme.AppThemeStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val appContext = getApplication<Application>().applicationContext
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        PlayBillingStore.init(appContext)
        viewModelScope.launch {
            ThemeStyleStore.observe(appContext).collect { style ->
                _uiState.value = _uiState.value.copy(themeStyle = style)
            }
        }
        viewModelScope.launch {
            SubscriptionStore.observe(appContext).collect { state ->
                _uiState.value = _uiState.value.copy(subscriptionState = state)
            }
        }
        viewModelScope.launch {
            PlayBillingStore.observeState().collect { state ->
                if (state != null) {
                    _uiState.value = _uiState.value.copy(subscriptionState = state)
                }
            }
        }
        viewModelScope.launch {
            TranslationModelStore.observeStatuses().collect { statuses ->
                _uiState.value = _uiState.value.copy(translationPackStatuses = statuses)
            }
        }
        viewModelScope.launch {
            ResourcePackageRepository.observeModelPacks(appContext).collect { packs ->
                _uiState.value = _uiState.value.copy(
                    modelPacks = packs,
                    storageSummary = ResourcePackageRepository.buildStorageSummary(appContext)
                )
            }
        }
        viewModelScope.launch {
            ResourcePackageRepository.observeCityPacks(appContext).collect { packs ->
                _uiState.value = _uiState.value.copy(
                    cityPacks = packs,
                    storageSummary = ResourcePackageRepository.buildStorageSummary(appContext)
                )
            }
        }
        viewModelScope.launch {
            BehaviorPreferencesStore.observeOfflineOnly(appContext).collect { value ->
                _uiState.value = _uiState.value.copy(offlineOnlyMode = value)
            }
        }
        viewModelScope.launch {
            BehaviorPreferencesStore.observeAutoDownloadWifi(appContext).collect { value ->
                _uiState.value = _uiState.value.copy(autoDownloadOnWifi = value)
            }
        }
        viewModelScope.launch {
            BehaviorPreferencesStore.observeHighQualityOcr(appContext).collect { value ->
                _uiState.value = _uiState.value.copy(highQualityOcr = value)
            }
        }
        refreshResources()
    }

    fun refreshResources() {
        viewModelScope.launch {
            TranslationModelStore.refresh()
            _uiState.value = _uiState.value.copy(
                storageSummary = ResourcePackageRepository.buildStorageSummary(appContext)
            )
        }
    }

    fun setThemeStyle(style: AppThemeStyle) {
        viewModelScope.launch {
            ThemeStyleStore.set(appContext, style)
        }
    }

    fun setSubscriptionState(state: LocalSubscriptionState) {
        viewModelScope.launch {
            SubscriptionStore.set(appContext, state)
        }
    }

    fun subscribe(activity: Activity) {
        PlayBillingStore.launchSubscribe(activity)
    }

    fun restorePurchase() {
        PlayBillingStore.restorePurchases()
    }

    fun manageSubscription(context: Context) {
        PlayBillingStore.openManageSubscriptions(context)
    }

    fun setOfflineOnlyMode(value: Boolean) {
        viewModelScope.launch { BehaviorPreferencesStore.setOfflineOnly(appContext, value) }
    }

    fun setAutoDownloadOnWifi(value: Boolean) {
        viewModelScope.launch { BehaviorPreferencesStore.setAutoDownloadWifi(appContext, value) }
    }

    fun setHighQualityOcr(value: Boolean) {
        viewModelScope.launch { BehaviorPreferencesStore.setHighQualityOcr(appContext, value) }
    }

    fun installModelPack(id: String) {
        viewModelScope.launch {
            ResourcePackageRepository.installModelPack(appContext, id)
        }
    }

    fun removeModelPack(id: String) {
        viewModelScope.launch {
            ResourcePackageRepository.removeModelPack(appContext, id)
        }
    }

    fun downloadCityPack(id: String) {
        viewModelScope.launch {
            ResourcePackageRepository.downloadCityPack(appContext, id)
        }
    }

    fun deleteCityPack(id: String) {
        viewModelScope.launch {
            ResourcePackageRepository.deleteCityPack(appContext, id)
        }
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            appContext.cacheDir.deleteRecursively()
            appContext.cacheDir.mkdirs()
        }
    }
}
