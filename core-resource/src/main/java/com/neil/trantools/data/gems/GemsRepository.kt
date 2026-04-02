package com.neil.trantools.data.gems

import android.content.Context
import org.json.JSONArray

object GemsRepository {
    private val cache = mutableMapOf<String, List<GemPoi>>()

    fun loadPois(context: Context): List<GemPoi> {
        val language = context.resources.configuration.locales[0]?.language ?: "en"
        return cache.getOrPut(language) {
            val assetName = if (language.startsWith("zh")) {
                "gems_pois_zh.json"
            } else {
                "gems_pois_en.json"
            }
            context.assets.open(assetName).bufferedReader().use { reader ->
                parsePois(reader.readText())
            }
        }
    }

    fun search(
        pois: List<GemPoi>,
        query: String,
        category: GemCategory?,
        currentLocation: GeoPoint?,
    ): List<GemPoiWithDistance> {
        val normalizedQuery = query.trim().lowercase()
        return pois
            .filter { poi ->
                val matchesCategory = category == null || poi.category == category
                val matchesQuery = normalizedQuery.isBlank() || buildString {
                    append(poi.title)
                    append(' ')
                    append(poi.summary)
                    append(' ')
                    append(poi.tip)
                    append(' ')
                    append(poi.address)
                    append(' ')
                    append(poi.tags.joinToString(" "))
                }.lowercase().contains(normalizedQuery)
                matchesCategory && matchesQuery
            }
            .map { poi ->
                GemPoiWithDistance(
                    poi = poi,
                    distanceMeters = currentLocation?.let {
                        distanceMeters(it.latitude, it.longitude, poi.latitude, poi.longitude)
                    }
                )
            }
            .sortedWith(
                compareBy<GemPoiWithDistance> { it.distanceMeters ?: Double.MAX_VALUE }
                    .thenByDescending { it.poi.rating }
            )
    }

    fun findById(
        pois: List<GemPoi>,
        id: String,
    ): GemPoi? = pois.firstOrNull { it.id == id }

    private fun parsePois(json: String): List<GemPoi> {
        val array = JSONArray(json)
        return buildList {
            for (index in 0 until array.length()) {
                val item = array.getJSONObject(index)
                add(
                    GemPoi(
                        id = item.getString("id"),
                        title = item.getString("title"),
                        category = GemCategory.valueOf(item.getString("category")),
                        summary = item.getString("summary"),
                        tip = item.getString("tip"),
                        address = item.getString("address"),
                        latitude = item.getDouble("latitude"),
                        longitude = item.getDouble("longitude"),
                        rating = item.getDouble("rating"),
                        tags = item.getJSONArray("tags").toStringList(),
                        bestTime = item.optString("bestTime"),
                        recommendedDuration = item.optString("recommendedDuration"),
                        budgetLevel = item.optString("budgetLevel")
                    )
                )
            }
        }
    }

    private fun distanceMeters(
        fromLatitude: Double,
        fromLongitude: Double,
        toLatitude: Double,
        toLongitude: Double,
    ): Double {
        val earthRadiusMeters = 6_371_000.0
        val dLat = Math.toRadians(toLatitude - fromLatitude)
        val dLon = Math.toRadians(toLongitude - fromLongitude)
        val lat1 = Math.toRadians(fromLatitude)
        val lat2 = Math.toRadians(toLatitude)
        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
            kotlin.math.cos(lat1) * kotlin.math.cos(lat2) *
            kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return earthRadiusMeters * c
    }
}

data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
    val label: String,
    val isFallback: Boolean,
)

data class GemPoiWithDistance(
    val poi: GemPoi,
    val distanceMeters: Double?,
)

private fun JSONArray.toStringList(): List<String> {
    return buildList {
        for (index in 0 until length()) {
            add(getString(index))
        }
    }
}
