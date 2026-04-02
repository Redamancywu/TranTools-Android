package com.neil.trantools.data.gems

import android.content.Context
import com.neil.trantools.data.content.LocalContentStore
import com.neil.trantools.data.content.PoiEntity
import com.neil.trantools.data.content.PoiFtsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray

object GemsRepository {
    private val cache = mutableMapOf<String, List<GemPoi>>()

    fun loadPois(context: Context): List<GemPoi> {
        val language = context.resources.configuration.locales[0]?.language ?: "en"
        val indexedPois = runCatching {
            runBlocking { LocalContentStore.getPois(language).map { it.toDomain() } }
        }.getOrDefault(emptyList())
        if (indexedPois.isNotEmpty()) {
            cache[language] = indexedPois
            return indexedPois
        }
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

    suspend fun syncIndexIfNeeded(context: Context) {
        val language = context.resources.configuration.locales[0]?.language ?: "en"
        if (LocalContentStore.poiCount(language) > 0) return
        val pois = withContext(Dispatchers.IO) {
            val assetName = if (language.startsWith("zh")) {
                "gems_pois_zh.json"
            } else {
                "gems_pois_en.json"
            }
            context.assets.open(assetName).bufferedReader().use { reader ->
                parsePois(reader.readText())
            }
        }
        LocalContentStore.replacePois(
            language = language,
            entities = pois.map { it.toEntity(language) },
            ftsEntities = pois.mapIndexed { index, poi ->
                poi.toFtsEntity(language = language, rowId = index + 1)
            }
        )
        cache[language] = pois
    }

    suspend fun searchIndexedPois(
        context: Context,
        query: String,
        limit: Int = 12,
    ): List<GemPoi> {
        syncIndexIfNeeded(context)
        val language = context.resources.configuration.locales[0]?.language ?: "en"
        val dbPois = LocalContentStore.getPois(language)
        if (query.isBlank()) return dbPois.map { it.toDomain() }.take(limit)
        val ids = LocalContentStore.searchPoiIds(
            language = language,
            query = buildFtsQuery(query),
            limit = limit
        )
        if (ids.isEmpty()) return dbPois.map { it.toDomain() }.take(limit)
        val byId = dbPois.associateBy { it.id }
        return ids.mapNotNull { id -> byId[id]?.toDomain() }
    }

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

private fun GemPoi.toEntity(language: String): PoiEntity {
    return PoiEntity(
        id = id,
        language = language,
        title = title,
        category = category.name,
        summary = summary,
        tip = tip,
        address = address,
        latitude = latitude,
        longitude = longitude,
        rating = rating,
        tagsBlob = tags.joinToString(separator = "\u001F"),
        bestTime = bestTime,
        recommendedDuration = recommendedDuration,
        budgetLevel = budgetLevel
    )
}

private fun GemPoi.toFtsEntity(
    language: String,
    rowId: Int,
): PoiFtsEntity {
    return PoiFtsEntity(
        rowId = rowId,
        poiId = id,
        language = language,
        title = title,
        category = category.name,
        summary = summary,
        tip = tip,
        address = address,
        tags = tags.joinToString(" ")
    )
}

private fun PoiEntity.toDomain(): GemPoi {
    return GemPoi(
        id = id,
        title = title,
        category = GemCategory.valueOf(category),
        summary = summary,
        tip = tip,
        address = address,
        latitude = latitude,
        longitude = longitude,
        rating = rating,
        tags = tagsBlob.split("\u001F").filter { it.isNotBlank() },
        bestTime = bestTime,
        recommendedDuration = recommendedDuration,
        budgetLevel = budgetLevel
    )
}

private fun buildFtsQuery(raw: String): String {
    return raw.trim()
        .split(Regex("[^\\p{L}\\p{N}]+"))
        .filter { it.length >= 2 }
        .joinToString(" OR ") { token -> "$token*" }
        .ifBlank { raw.trim() }
}
