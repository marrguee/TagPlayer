package com.example.tagplayer.core.data

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.tagplayer.core.domain.ProvideMediaStoreHandler
import com.example.tagplayer.core.domain.ProvidePlayerService
import kotlin.reflect.KClass

interface ForegroundWrapper {
    fun scanMedia()
    fun scanNewMedia()
    fun playMedia(id: Long)
    fun fetchNewSong(uri: Uri)
    fun deleteSong()

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

        override fun scanMedia() {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<MediaWorker>(),
                ExistingWorkPolicy.KEEP,
                MediaWorker::class
            )
        }

        override fun scanNewMedia() {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<ListenMediaWorker>(),
                ExistingWorkPolicy.KEEP,
                ListenMediaWorker::class
            )
        }

        override fun playMedia(id: Long) {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<PlaySongWorker>()
                    .setInputData(workDataOf(PLAY_MEDIA_ID_KEY to id)),
                ExistingWorkPolicy.KEEP,
                PlaySongWorker::class
            )
        }

        override fun fetchNewSong(uri: Uri) {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<FetchSongWorker>().setInputData(
                    workDataOf(URI_KEY to uri)
                ),
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                FetchSongWorker::class
            )
        }

        override fun deleteSong() {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<DeleteSongWorker>(),
                ExistingWorkPolicy.KEEP,
                DeleteSongWorker::class
            )
        }

        companion object {
            private const val PLAY_MEDIA_ID_KEY = "PLAY_MEDIA_ID_KEY"
            private const val URI_KEY = "URI_KEY"
        }
    }
}


class MediaWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result = try {
        (applicationContext as ProvideMediaStoreHandler).mediaStoreHandler().scan()
        Result.success()
    } catch (e: Exception) {
        Result.failure()
    }
}

class ListenMediaWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result = try {
        //(applicationContext as ProvideMediaStoreHandler).mediaStoreHandler().checkNewFiles()
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

class FetchSongWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val uri: Uri = inputData.getString(inputData.keyValueMap.keys.first())?.toUri()
            ?: return Result.failure()
        (applicationContext as ProvideMediaStoreHandler).mediaStoreHandler().scanNewFile(uri)
        return Result.success()
    }
}

class DeleteSongWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return Result.success()
    }
}