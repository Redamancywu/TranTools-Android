package com.neil.trantools.feature.gems

import com.neil.trantools.data.gems.GemCategory
import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.gems.GemPoiWithDistance
import com.neil.trantools.data.gems.GeoPoint
import com.neil.trantools.data.gems.GemsRepository

data class GemsUiState(
    val allPois: List<GemPoi> = emptyList(),
    val query: String = "",
    val selectedCategory: GemCategory? = null,
    val favorites: Set<String> = emptySet(),
    val currentLocation: GeoPoint? = null,
    val hasLocationPermission: Boolean = false,
    val isRefreshingLocation: Boolean = false,
) {
    val visiblePois: List<GemPoiWithDistance>
        get() = GemsRepository.search(
            pois = allPois,
            query = query,
            category = selectedCategory,
            currentLocation = currentLocation
        )

    val featuredPois: List<GemPoiWithDistance>
        get() = visiblePois.take(3)
}
