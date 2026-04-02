package com.neil.trantools.feature.gems

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.gems.GemCategory
import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.gems.GeoPoint
import com.neil.trantools.data.gems.GemsPreferencesStore
import com.neil.trantools.data.gems.GemsRepository
import com.neil.trantools.data.settings.ResourcePackageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GemsViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val appContext = getApplication<Application>().applicationContext
    private val allPois = MutableStateFlow(GemsRepository.loadPois(appContext))
    private val query = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow<GemCategory?>(null)
    private val currentLocation = MutableStateFlow(defaultFallbackLocation(appContext))
    private val hasLocationPermission = MutableStateFlow(hasLocationPermission(appContext))
    private val isRefreshingLocation = MutableStateFlow(false)

    private val filterState = combine(
        query,
        selectedCategory,
        currentLocation,
        hasLocationPermission,
        isRefreshingLocation
    ) { currentQuery, category, location, permission, refreshing ->
        GemsFilterState(
            query = currentQuery,
            category = category,
            location = location,
            permission = permission,
            refreshing = refreshing
        )
    }

    val uiState: StateFlow<GemsUiState> = combine(
        allPois,
        GemsPreferencesStore.observeFavoriteIds(appContext),
        filterState
    ) { pois, favorites, filter ->
        GemsUiState(
            allPois = pois,
            query = filter.query,
            selectedCategory = filter.category,
            favorites = favorites,
            currentLocation = filter.location,
            hasLocationPermission = filter.permission,
            isRefreshingLocation = filter.refreshing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GemsUiState()
    )

    init {
        viewModelScope.launch {
            ResourcePackageRepository.observeCityPacks(appContext).collect { cityPacks ->
                val kyotoInstalled = cityPacks.any { it.id == "kyoto" && it.installed }
                allPois.value = if (kyotoInstalled) {
                    GemsRepository.loadPois(appContext)
                } else {
                    emptyList()
                }
            }
        }
    }

    fun setQuery(value: String) {
        query.value = value
    }

    fun setCategory(category: GemCategory?) {
        selectedCategory.value = category
    }

    fun onLocationPermissionChanged(granted: Boolean) {
        hasLocationPermission.value = granted
        if (granted) {
            refreshLocation()
        } else {
            currentLocation.value = defaultFallbackLocation(appContext)
        }
    }

    fun refreshLocation() {
        viewModelScope.launch {
            isRefreshingLocation.value = true
            val location = withContext(Dispatchers.IO) {
                lastKnownLocation(appContext)
            }
            currentLocation.value = location ?: defaultFallbackLocation(appContext)
            isRefreshingLocation.value = false
        }
    }

    fun toggleFavorite(poiId: String) {
        viewModelScope.launch {
            GemsPreferencesStore.toggleFavorite(appContext, poiId)
        }
    }

    fun findPoi(id: String): GemPoi? = GemsRepository.findById(allPois.value, id)

    companion object {
        private fun hasLocationPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun lastKnownLocation(context: Context): GeoPoint? {
            if (!hasLocationPermission(context)) return null
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
            val providers = manager.getProviders(true)
            val best = providers
                .mapNotNull { provider -> runCatching { manager.getLastKnownLocation(provider) }.getOrNull() }
                .maxByOrNull(Location::getTime)
                ?: return null

            return GeoPoint(
                latitude = best.latitude,
                longitude = best.longitude,
                label = context.getString(R.string.gems_location_device),
                isFallback = false
            )
        }

        private fun defaultFallbackLocation(context: Context? = null): GeoPoint {
            return GeoPoint(
                latitude = 35.0116,
                longitude = 135.7681,
                label = context?.getString(R.string.gems_location_demo) ?: "Kyoto demo center",
                isFallback = true
            )
        }
    }
}

private data class GemsFilterState(
    val query: String,
    val category: GemCategory?,
    val location: GeoPoint,
    val permission: Boolean,
    val refreshing: Boolean,
)
