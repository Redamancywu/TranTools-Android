package com.neil.trantools.data.settings

import android.app.ActivityManager
import android.content.Context
import com.neil.trantools.data.gems.GemsRepository
import com.neil.trantools.data.translation.TranslationModelStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.io.File

data class ModelPackInfo(
    val id: String,
    val title: String,
    val description: String,
    val sizeMb: Int,
    val installed: Boolean,
    val premium: Boolean,
    val installStatus: ModelPackInstallStatus = if (installed) ModelPackInstallStatus.Ready else ModelPackInstallStatus.NotInstalled,
    val progressPercent: Int = 0,
    val errorMessage: String? = null,
    val runtimeLabel: String? = null,
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

private data class DownloadableModelPackDefinition(
    val id: String,
    val title: String,
    val description: String,
    val sizeMb: Int,
    val premium: Boolean,
    val fileName: String,
    val downloadUrl: String,
    val runtimeLabel: String,
    val sha256: String? = null,
)

object ResourcePackageRepository {
    fun observeModelPacks(context: Context): Flow<List<ModelPackInfo>> {
        return combine(
            TranslationModelStore.observeStatuses(),
            ModelPackStore.observeRecords(context)
        ) { translationStatuses, records ->
            val llmRecord = records[assistantQwenPack.id]
            val ocrRecord = records["ocr-advanced"]
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
                    installed = ocrRecord?.installed == true,
                    premium = true
                ),
                ModelPackInfo(
                    id = assistantQwenPack.id,
                    title = assistantQwenPack.title,
                    description = assistantQwenPack.description,
                    sizeMb = assistantQwenPack.sizeMb,
                    installed = llmRecord?.installed == true,
                    premium = assistantQwenPack.premium,
                    installStatus = llmRecord?.installStatus ?: ModelPackInstallStatus.NotInstalled,
                    progressPercent = llmRecord?.progressPercent ?: 0,
                    errorMessage = llmRecord?.errorMessage,
                    runtimeLabel = assistantQwenPack.runtimeLabel
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
        if (isPremiumPack(packId) && !hasPremiumAccess(context)) {
            ModelPackStore.setFailed(
                context = context,
                packId = packId,
                errorMessage = premiumRequiredError
            )
            return
        }
        if (packId == assistantQwenPack.id) {
            if (!canRunAssistantModel(context)) {
                ModelPackStore.setFailed(
                    context = context,
                    packId = packId,
                    errorMessage = assistantModelRamRequirementError
                )
                return
            }
            enqueuePrimaryAssistantPackDownload(context)
            return
        }
        ModelPackStore.setInstalled(context, packId, installed = true)
    }

    suspend fun removeModelPack(context: Context, packId: String) {
        if (packId == "translate-core") return
        if (packId == assistantQwenPack.id) {
            val localPath = ModelPackStore.getRecord(context, packId)?.localPath
            localPath?.let { path ->
                runCatching { File(path).delete() }
            }
        }
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
        val modelPackMb = dirSizeMb(modelPackDir(context))
        val modelMb = cacheMb + dbMb + modelPackMb
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

    suspend fun getReadyModelPath(
        context: Context,
        packId: String = assistantQwenPack.id,
    ): String? {
        val record = ModelPackStore.getRecord(context, packId) ?: return null
        val localPath = record.localPath ?: return null
        return localPath.takeIf {
            File(it).exists() && record.installStatus == ModelPackInstallStatus.Ready
        }
    }

    private suspend fun enqueuePrimaryAssistantPackDownload(context: Context) {
        ModelPackStore.setDownloading(context, assistantQwenPack.id, progressPercent = 1)
        ModelPackDownloadWorker.enqueue(
            context = context,
            packId = assistantQwenPack.id,
            fileName = assistantQwenPack.fileName,
            downloadUrl = assistantQwenPack.downloadUrl,
            expectedSha256 = assistantQwenPack.sha256
        )
    }

    private fun modelPackDir(context: Context): File {
        return File(context.filesDir, "model-packs")
    }

    private suspend fun hasPremiumAccess(context: Context): Boolean {
        val billingState = PlayBillingStore.currentState()
        if (billingState != null) {
            return billingState == LocalSubscriptionState.Pro
        }
        return SubscriptionStore.get(context) == LocalSubscriptionState.Pro
    }

    private fun isPremiumPack(packId: String): Boolean {
        return packId in premiumModelPackIds
    }

    private fun canRunAssistantModel(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return true
        val info = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(info)
        return info.totalMem >= minAssistantModelRamBytes
    }

    private val assistantQwenPack = DownloadableModelPackDefinition(
        id = "qwen2.5-1.5b-instruct-q8",
        title = "Qwen2.5 1.5B Instruct",
        description = "On-device multilingual assistant model tuned for grounded travel answers",
        sizeMb = 1600,
        premium = true,
        fileName = "Qwen2.5-1.5B-Instruct_multi-prefill-seq_q8_ekv4096.task",
        downloadUrl = "https://huggingface.co/litert-community/Qwen2.5-1.5B-Instruct/resolve/main/Qwen2.5-1.5B-Instruct_multi-prefill-seq_q8_ekv4096.task",
        runtimeLabel = "MediaPipe LLM",
        sha256 = null
    )

    private val premiumModelPackIds = setOf(
        "ocr-advanced",
        assistantQwenPack.id
    )

    private const val premiumRequiredError = "TranTools Pro subscription required."
    private const val assistantModelRamRequirementError = "Device memory is below the minimum requirement for this model."
    private const val minAssistantModelRamBytes = 4L * 1024L * 1024L * 1024L
}
