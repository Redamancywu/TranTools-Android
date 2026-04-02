package com.neil.trantools.data.settings

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CancellationException

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
            if (throwable is CancellationException) throw throwable
            tempFile.delete()
            val failure = classifyFailure(throwable)
            if (failure.retryable && runAttemptCount < MAX_RETRY_ATTEMPTS) {
                ModelPackStore.setDownloading(applicationContext, packId, 1)
                Result.retry()
            } else {
                ModelPackStore.setFailed(
                    context = applicationContext,
                    packId = packId,
                    errorMessage = failure.message
                )
                Result.failure()
            }
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
        try {
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                throw DownloadFailure(
                    message = when (responseCode) {
                        401, 403 -> "Model download requires accepted model license access."
                        404 -> "Model package not found on server."
                        else -> "HTTP $responseCode"
                    },
                    retryable = isRetryableHttpCode(responseCode)
                )
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
            onProgress(100)
        } catch (timeout: SocketTimeoutException) {
            throw DownloadFailure(
                message = "Download timed out. Please retry.",
                retryable = true,
                cause = timeout
            )
        } catch (unknownHost: UnknownHostException) {
            throw DownloadFailure(
                message = "Network unavailable. Check connection and retry.",
                retryable = true,
                cause = unknownHost
            )
        } catch (io: IOException) {
            throw DownloadFailure(
                message = io.message ?: "Download failed due to network error.",
                retryable = true,
                cause = io
            )
        } finally {
            connection.disconnect()
        }
    }

    companion object {
        private const val KEY_PACK_ID = "pack_id"
        private const val KEY_FILE_NAME = "file_name"
        private const val KEY_DOWNLOAD_URL = "download_url"
        private const val KEY_PROGRESS = "progress"
        private const val MAX_RETRY_ATTEMPTS = 2
        private const val RETRY_BACKOFF_SECONDS = 30L

        fun enqueue(
            context: Context,
            packId: String,
            fileName: String,
            downloadUrl: String,
        ) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<ModelPackDownloadWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    RETRY_BACKOFF_SECONDS,
                    TimeUnit.SECONDS
                )
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
                ExistingWorkPolicy.REPLACE,
                request
            )
        }

        private fun isRetryableHttpCode(code: Int): Boolean {
            return code == 408 || code == 429 || code in 500..599
        }

        private fun classifyFailure(throwable: Throwable): DownloadFailure {
            return when (throwable) {
                is DownloadFailure -> throwable
                is SocketTimeoutException -> DownloadFailure(
                    message = "Download timed out. Please retry.",
                    retryable = true,
                    cause = throwable
                )

                is UnknownHostException -> DownloadFailure(
                    message = "Network unavailable. Check connection and retry.",
                    retryable = true,
                    cause = throwable
                )

                is IOException -> DownloadFailure(
                    message = throwable.message ?: "Download failed due to network error.",
                    retryable = true,
                    cause = throwable
                )

                else -> DownloadFailure(
                    message = throwable.message ?: "Model download failed.",
                    retryable = false,
                    cause = throwable
                )
            }
        }
    }
}

private data class DownloadFailure(
    override val message: String,
    val retryable: Boolean,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)
