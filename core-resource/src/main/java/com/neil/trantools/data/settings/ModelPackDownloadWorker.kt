package com.neil.trantools.data.settings

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class ModelPackDownloadWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val packId = inputData.getString(KEY_PACK_ID) ?: return Result.failure()
        val fileName = inputData.getString(KEY_FILE_NAME) ?: return Result.failure()
        val downloadUrl = inputData.getString(KEY_DOWNLOAD_URL) ?: return Result.failure()

        val destinationDir = File(applicationContext.filesDir, "model-packs")
        if (!destinationDir.exists()) destinationDir.mkdirs()
        val finalFile = File(destinationDir, fileName)
        val tempFile = File(destinationDir, "$fileName.download")

        return runCatching {
            ModelPackStore.setDownloading(applicationContext, packId, 1)
            downloadFile(downloadUrl, tempFile) { progress ->
                ModelPackStore.setDownloading(applicationContext, packId, progress)
                setProgress(
                    Data.Builder()
                        .putInt(KEY_PROGRESS, progress)
                        .build()
                )
            }
            if (finalFile.exists()) {
                finalFile.delete()
            }
            if (!tempFile.renameTo(finalFile)) {
                tempFile.copyTo(finalFile, overwrite = true)
                tempFile.delete()
            }
            ModelPackStore.setInstalled(
                context = applicationContext,
                packId = packId,
                installed = true,
                localPath = finalFile.absolutePath
            )
            Result.success()
        }.getOrElse { throwable ->
            tempFile.delete()
            ModelPackStore.setFailed(
                context = applicationContext,
                packId = packId,
                errorMessage = throwable.message ?: "Model download failed"
            )
            Result.retry()
        }
    }

    private suspend fun downloadFile(
        downloadUrl: String,
        destination: File,
        onProgress: suspend (Int) -> Unit,
    ) {
        val connection = (URL(downloadUrl).openConnection() as HttpURLConnection).apply {
            connectTimeout = 20_000
            readTimeout = 60_000
            requestMethod = "GET"
            instanceFollowRedirects = true
            setRequestProperty("User-Agent", "TranTools-Android/1.0")
        }
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode !in 200..299) {
            val details = when (responseCode) {
                401, 403 -> "Model download requires accepted model license access."
                else -> "HTTP $responseCode"
            }
            connection.disconnect()
            error(details)
        }

        val totalBytes = connection.contentLengthLong.coerceAtLeast(0L)
        destination.outputStream().use { output ->
            connection.inputStream.use { input ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var downloaded = 0L
                var read = input.read(buffer)
                while (read >= 0) {
                    output.write(buffer, 0, read)
                    downloaded += read
                    if (totalBytes > 0L) {
                        val progress = ((downloaded * 100L) / totalBytes).toInt().coerceIn(1, 99)
                        onProgress(progress)
                    }
                    read = input.read(buffer)
                }
            }
        }
        connection.disconnect()
        onProgress(100)
    }

    companion object {
        private const val KEY_PACK_ID = "pack_id"
        private const val KEY_FILE_NAME = "file_name"
        private const val KEY_DOWNLOAD_URL = "download_url"
        private const val KEY_PROGRESS = "progress"

        fun enqueue(
            context: Context,
            packId: String,
            fileName: String,
            downloadUrl: String,
        ) {
            val request = OneTimeWorkRequestBuilder<ModelPackDownloadWorker>()
                .setInputData(
                    Data.Builder()
                        .putString(KEY_PACK_ID, packId)
                        .putString(KEY_FILE_NAME, fileName)
                        .putString(KEY_DOWNLOAD_URL, downloadUrl)
                        .build()
                )
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "model-pack-$packId",
                androidx.work.ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }
}
