package com.neil.trantools.data.settings

import android.content.Context
import com.neil.trantools.data.gems.GemsRepository
import com.neil.trantools.data.translation.TranslationModelStore
import com.neil.trantools.data.wiki.WikiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

data class ModelPackInfo(
    val id: String,
    val title: String,
    val description: String,
    val sizeMb: Int,
    val installed: Boolean,
    val premium: Boolean,
)

data class CityPackInfo(
    val id: String,
    val title: String,
    val description: String,
    val sizeMb: Int,
    val poiCount: Int,
    val installed: Boolean,
)

data class StorageSummary(
    val usedMb: Int,
    val modelMb: Int,
    val cityMb: Int,
    val wikiMb: Int,
)

object ResourcePackageRepository {
    fun observeModelPacks(context: Context): Flow<List<ModelPackInfo>> {
        return combine(
            TranslationModelStore.observeStatuses(),
            ModelPackStore.observeInstalledIds(context)
        ) { translationStatuses, installedModelPackIds ->
            listOf(
                ModelPackInfo(
                    id = "translate-core",
                    title = "Translation Core",
                    description = "ML Kit language translation runtime",
                    sizeMb = 48,
                    installed = translationStatuses.any { it.isDownloaded },
                    premium = false
                ),
                ModelPackInfo(
                    id = "ocr-advanced",
                    title = "Advanced OCR",
                    description = "Higher quality menu and sign text extraction",
                    sizeMb = 96,
                    installed = "ocr-advanced" in installedModelPackIds,
                    premium = true
                ),
                ModelPackInfo(
                    id = "assistant-local",
                    title = "Local Assistant Orchestrator",
                    description = "Local retrieval and response composition pipeline",
                    sizeMb = 32,
                    installed = "assistant-local" in installedModelPackIds,
                    premium = false
                )
            )
        }
    }

    fun observeCityPacks(context: Context): Flow<List<CityPackInfo>> {
        return CityPackStore.observeInstalledIds(context).map { installedIds ->
            buildCityPacks(context = context, installedCityPackIds = installedIds)
        }
    }

    fun loadCityPacks(context: Context): List<CityPackInfo> {
        return buildCityPacks(context = context, installedCityPackIds = setOf("kyoto"))
    }

    suspend fun downloadCityPack(context: Context, packId: String) {
        CityPackStore.setInstalled(context, packId, installed = true)
    }

    suspend fun deleteCityPack(context: Context, packId: String) {
        CityPackStore.setInstalled(context, packId, installed = false)
    }

    suspend fun installModelPack(context: Context, packId: String) {
        if (packId == "translate-core") return
        ModelPackStore.setInstalled(context, packId, installed = true)
    }

    suspend fun removeModelPack(context: Context, packId: String) {
        if (packId == "translate-core") return
        ModelPackStore.setInstalled(context, packId, installed = false)
    }

    private fun buildCityPacks(
        context: Context,
        installedCityPackIds: Set<String>,
        pois: List<com.neil.trantools.data.gems.GemPoi> = GemsRepository.loadPois(context),
    ): List<CityPackInfo> {
        return listOf(
            CityPackInfo(
                id = "kyoto",
                title = "Kyoto Offline Pack",
                description = "Temples, markets, shrines, and curated walking spots",
                sizeMb = 42,
                poiCount = pois.size,
                installed = "kyoto" in installedCityPackIds
            ),
            CityPackInfo(
                id = "osaka",
                title = "Osaka Offline Pack",
                description = "Food districts and compact city exploration bundle",
                sizeMb = 36,
                poiCount = 0,
                installed = "osaka" in installedCityPackIds
            )
        )
    }

    fun buildStorageSummary(context: Context): StorageSummary {
        val wikiMb = assetSizeMb(context, "wiki_articles_en.json") + assetSizeMb(context, "wiki_articles_zh.json")
        val cityMb = assetSizeMb(context, "gems_pois_en.json") + assetSizeMb(context, "gems_pois_zh.json")
        val cacheMb = dirSizeMb(context.cacheDir)
        val dbMb = dbSizeMb(context, "tran_tools.db")
        val modelMb = cacheMb + dbMb
        return StorageSummary(
            usedMb = wikiMb + cityMb + modelMb,
            modelMb = modelMb,
            cityMb = cityMb,
            wikiMb = wikiMb
        )
    }

    private fun assetSizeMb(context: Context, name: String): Int {
        return runCatching {
            val bytes = context.assets.open(name).use { it.readBytes().size }
            (bytes / 1024f / 1024f).toInt().coerceAtLeast(1)
        }.getOrDefault(0)
    }

    private fun dirSizeMb(dir: java.io.File): Int {
        if (!dir.exists()) return 0
        var total = 0L
        dir.walkTopDown().forEach { file ->
            if (file.isFile) total += file.length()
        }
        return (total / 1024f / 1024f).toInt()
    }

    private fun dbSizeMb(context: Context, dbName: String): Int {
        val dbFile = context.getDatabasePath(dbName)
        if (!dbFile.exists()) return 0
        val total = dbFile.length() + runCatching {
            context.getDatabasePath("$dbName-wal").let { if (it.exists()) it.length() else 0L }
        }.getOrDefault(0L) + runCatching {
            context.getDatabasePath("$dbName-shm").let { if (it.exists()) it.length() else 0L }
        }.getOrDefault(0L)
        return (total / 1024f / 1024f).toInt().coerceAtLeast(0)
    }
}
