package com.example.tagplayer.core.domain

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.tagplayer.core.data.ProvideMediaStoreHandler

interface ForegroundWrapper {
    fun scanMedia()
    fun fetchNewSong()
    fun deleteSong()

    class Base(
        private val workManager: WorkManager
    ) : ForegroundWrapper {

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