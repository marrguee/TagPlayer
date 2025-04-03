package com.example.tagplayer.core.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.tagplayer.core.domain.ProvideMediaStoreHandler
import com.example.tagplayer.core.domain.ProvidePlayerService
import kotlin.reflect.KClass


interface ForegroundWrapper {
    fun scanMedia()
    fun playMedia(id: Long)
    fun fetchNewSong(uri: String)

    class Base(
        private val workManager: WorkManager
    ) : ForegroundWrapper {

        private val startWorker: (OneTimeWorkRequest.Builder, ExistingWorkPolicy, KClass<*>) -> Unit =
            { request: OneTimeWorkRequest.Builder, policy: ExistingWorkPolicy, clazz: KClass<*> ->
                workManager.beginUniqueWork(
                    clazz.simpleName.toString(),
                    policy,
                    request.build()
                ).enqueue()
            }
        @UnstableApi
        override fun scanMedia() {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<MediaWorker>(),
                ExistingWorkPolicy.KEEP,
                MediaWorker::class
            )
        }

        override fun playMedia(id: Long) {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<PlaySongWorker>()
                    .setInputData(workDataOf(PLAY_MEDIA_ID_KEY to id))
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST),
                ExistingWorkPolicy.KEEP,
                PlaySongWorker::class
            )
        }
        @UnstableApi
        override fun fetchNewSong(uri: String) {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<FetchSongWorker>().setInputData(
                    workDataOf(SONG_ID_KEY to uri)
                ),
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                FetchSongWorker::class
            )
        }

        companion object {
            private const val PLAY_MEDIA_ID_KEY = "PLAY_MEDIA_ID_KEY"
            private const val SONG_ID_KEY = "SONG_ID_KEY"
        }
    }
}

@UnstableApi
class MediaWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result = try {
        (applicationContext as ProvideMediaStoreHandler).mediaStoreHandler()
                .scan(applicationContext)
        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }
}

class PlaySongWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val songId: Long = inputData.getLong(inputData.keyValueMap.keys.first(), -1)
        if (songId == -1L) return Result.failure()
        (applicationContext as ProvidePlayerService).start(songId)
        return Result.success()
    }
}

@UnstableApi
class FetchSongWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val uri: Uri = inputData.getString(inputData.keyValueMap.keys.first())?.toUri()
                ?: return Result.failure()
            (applicationContext as ProvideMediaStoreHandler).mediaStoreHandler().scanNewFile(uri)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}