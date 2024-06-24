package com.example.tagplayer.core.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.tagplayer.core.ProvidePlayerService

interface ForegroundWrapper {
    fun scanMedia()
    fun playMedia(id: Long)
    fun fetchNewSong()
    fun deleteSong()

    class Base(
        private val workManager: WorkManager
    ) : ForegroundWrapper {
        private val playMediaIdKey = "playMediaIdKey"

        private val startWorker: (OneTimeWorkRequest.Builder, String) -> Unit = {
            request: OneTimeWorkRequest.Builder, name: String ->
                workManager.beginUniqueWork(
                    name,
                    ExistingWorkPolicy.KEEP,
                    request.build()
                ).enqueue()
        }
        override fun scanMedia() {
            startWorker.invoke(OneTimeWorkRequestBuilder<MediaWorker>(), "MediaWorker")
        }

        override fun playMedia(id: Long) {
            startWorker.invoke(
                OneTimeWorkRequestBuilder<PlaySongWorker>()
                    .setInputData(workDataOf(playMediaIdKey to id)),
                "PlaySongWorker"
            )
        }

        override fun fetchNewSong() {
            startWorker.invoke(OneTimeWorkRequestBuilder<FetchSongWorker>(), "FetchSongWorker")
        }

        override fun deleteSong() {
            startWorker.invoke(OneTimeWorkRequestBuilder<DeleteSongWorker>(), "DeleteSongWorker")
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

class PlaySongWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val defaultId: Long = -1
        val id = inputData.getLong("playMediaIdKey", defaultId)
        if (id == defaultId) return Result.failure()
        val uri = (applicationContext as ProvideMediaStoreHandler)
            .mediaStoreHandler().uri(id)
        (applicationContext as ProvidePlayerService).start(uri)
        return Result.success()
    }
}

class FetchSongWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
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