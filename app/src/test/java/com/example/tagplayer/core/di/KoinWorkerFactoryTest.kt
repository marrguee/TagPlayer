package com.example.tagplayer.core.di

import android.content.Context
import androidx.work.WorkerParameters
import com.example.tagplayer.core.data.FetchSongWorker
import com.example.tagplayer.core.data.MediaWorker
import com.example.tagplayer.core.data.PlaySongWorker
import com.example.tagplayer.home.data.HandleMediaStore
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class KoinWorkerFactoryTest {
    private val context = mock<Context>()
    private val workerParameters = mock<WorkerParameters>()
    private val mediaStoreHandler = mock<HandleMediaStore>()

    @After
    fun tearDown() = stopKoin()

    @Before
    fun setup() {
        whenever(context.applicationContext).thenReturn(context)
        startKoin {
            androidContext(context)
            modules(
                coreModule,
                module {
                    single<HandleMediaStore> { mediaStoreHandler }
                }
            )
        }
    }

    @Test
    fun `creates play song worker with Android context and worker parameters`() {
        val worker = KoinWorkerFactory().createWorker(
            context,
            PlaySongWorker::class.java.name,
            workerParameters
        )

        assertTrue(worker is PlaySongWorker)
    }

    @Test
    fun `creates media worker with Android context and worker parameters`() {
        val worker = KoinWorkerFactory().createWorker(
            context,
            MediaWorker::class.java.name,
            workerParameters
        )

        assertTrue(worker is MediaWorker)
    }

    @Test
    fun `creates fetch song worker with Android context and worker parameters`() {
        val worker = KoinWorkerFactory().createWorker(
            context,
            FetchSongWorker::class.java.name,
            workerParameters
        )

        assertTrue(worker is FetchSongWorker)
    }
}