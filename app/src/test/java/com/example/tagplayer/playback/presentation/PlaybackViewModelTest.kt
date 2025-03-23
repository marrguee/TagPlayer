package com.example.tagplayer.playback.presentation

import android.annotation.SuppressLint
import android.content.IntentSender
import android.net.Uri
import android.os.Bundle
import androidx.media3.session.MediaController
import com.example.tagplayer.FakeAllObservable
import com.example.tagplayer.FakeDispatcherList
import com.example.tagplayer.FakeNavigation
import com.example.tagplayer.FakeRunAsync
import com.example.tagplayer.core.media_service.HandleMediaExtras
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.playback.domain.PlaybackInteractor
import com.example.tagplayer.playback.domain.SongDetailsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class PlaybackViewModelTest {
    private lateinit var viewModel: PlaybackViewModel

    private lateinit var runAsync: FakeRunAsync
    private lateinit var observable: FakeObservable
    private lateinit var interactor: FakeInteractor
    private lateinit var mapper: FakeMapper
    private lateinit var navigation: FakeNavigation
    private lateinit var mediaExtras: FakeHandleMediaExtras
    private lateinit var handleConnection: FakeHandleConnection

    @SuppressLint("CheckResult")
    @Before
    fun setUp() {
        runAsync = FakeRunAsync.Base()
        observable = FakeObservable.Base()
        interactor = FakeInteractor.Base()
        mapper = FakeMapper.Base(observable, FakeDispatcherList.Base())
        navigation = FakeNavigation.Base()
        mediaExtras = FakeHandleMediaExtras.Base()
        handleConnection = FakeHandleConnection.Base()

        viewModel = PlaybackViewModel(
            runAsync,
            observable,
            interactor,
            mapper,
            navigation,
            mediaExtras,
            handleConnection
        )

        Mockito.mockStatic(Uri::class.java)
        Mockito.`when`(Uri.parse(Mockito.anyString())).thenReturn(Mockito.mock(Uri::class.java))
    }

    @Test
    fun `main scenario`() {
        handleConnection.checkBuildCalled(1)
        val observer = object : CustomObserver<PlaybackState> {
            override fun update(data: PlaybackState) = Unit
        }
        viewModel.startGettingUpdates(observer)

        observable.checkObserver(observer)
        handleConnection.checkConnected(true)

        viewModel.stopGettingUpdates()

        handleConnection.checkConnected(false)
        observable.checkState(PlaybackState.Disconnected)
        observable.checkObserver(PlaybackObserver.Empty)

        viewModel.startGettingUpdates(observer)
        handleConnection.checkConnected(true)
        handleConnection.checkBuildCalled(1)

        viewModel.playPause()
        handleConnection.checkPlayPauseCalled(1)

        viewModel.rewind()
        handleConnection.checkRewindCalled(1)

        viewModel.clearMediaQueue()
        handleConnection.checkClearCalled(1)

        viewModel.deleteSong()
        runAsync.checkHandleCalled(1)
        runAsync.pingResult()
        interactor.checkDeleteCalled(1)

        viewModel.shareSong()
        runAsync.checkHandleCalled(2)
        runAsync.pingResult()
        interactor.checkShareCalled(1)
    }

    private interface FakeObservable : FakeAllObservable<PlaybackState> {
        class Base : FakeObservable,
            FakeAllObservable.Base<PlaybackState>(PlaybackState.Empty, PlaybackObserver.Empty)
    }

    private interface FakeInteractor : PlaybackInteractor {
        fun checkTagsCalled(times: Int)
        fun checkShareCalled(times: Int)
        fun checkDeleteCalled(times: Int)
        fun setDeleteResult(result: SongDetailsResponse)

        class Base : FakeInteractor {
            private var deleteResult: SongDetailsResponse = SongDetailsResponse.DeletionSucceed
            private var tagsCalled = 0
            private var shareCalled = 0
            private var deleteSongCalled = 0

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkShareCalled(times: Int) = assertEquals(times, shareCalled)

            override fun checkDeleteCalled(times: Int) = assertEquals(times, deleteSongCalled)

            override fun setDeleteResult(result: SongDetailsResponse) {
                deleteResult = result
            }

            override suspend fun deleteSong(songId: Long): SongDetailsResponse {
                deleteSongCalled++
                return deleteResult
            }

            override fun tags(id: Long): SongDetailsResponse {
                tagsCalled++
                return SongDetailsResponse.TagsFlow(flowOf())
            }

            override suspend fun share(id: Long): SongDetailsResponse {
                shareCalled++
                return SongDetailsResponse.SongUri(String())
            }
        }
    }

    private interface FakeMapper : SongDetailsResponse.Mapper {
        fun checkMapShareCalled(times: Int)

        class Base(
            private val observable: CustomObservable.UpdateUi<PlaybackState>,
            private val dispatcherList: FakeDispatcherList
        ) : FakeMapper {
            private var job: Job? = null
            private var mapShareCalled = 0

            override fun checkMapShareCalled(times: Int) = assertEquals(times, mapShareCalled)

            override fun mapUri(uri: Uri) {
                mapShareCalled++
            }

            override fun mapFlow(flow: Flow<List<TagPlaybackUi>>, coroutineScope: CoroutineScope) {
                job?.cancel()
                job = coroutineScope.launch(dispatcherList.ui()) {
                    flow.cancellable().collect {
                        observable.update(PlaybackState.UpdateTags(it))
                    }
                }
            }

            override fun mapError(error: String) {
                observable.update(PlaybackState.Error(error))
            }

            override fun mapIntentSender(intentSender: IntentSender) {
                observable.update(PlaybackState.PermissionRequired(intentSender))
            }

            override fun mapSucceed() {
                observable.update(PlaybackState.DeletingSuccess)
            }
        }
    }

    private interface FakeHandleMediaExtras : HandleMediaExtras.Obtain {
        class Base : FakeHandleMediaExtras {
            override fun getMediaId(bundle: Bundle?): Long = 0
            override fun getMediaUri(bundle: Bundle?): Uri = Uri.EMPTY
        }
    }

    private interface FakeHandleConnection : HandleConnection {
        fun checkBuildCalled(times: Int)
        fun checkPlayPauseCalled(times: Int)
        fun checkRewindCalled(times: Int)
        fun checkClearCalled(times: Int)
        fun checkSeekToCalled(times: Int)
        fun checkConnected(connected: Boolean)

        class Base : FakeHandleConnection {
            private var isConnected = false
            private var onConnectedBlock: ((MediaController) -> Unit)? = null
            private var onDisconnectedBlock: (() -> Unit)? = null
            private var buildCalled = 0
            private var controller: MediaController? = null

            private var playPauseCalled = 0
            private var rewindCalled = 0
            private var clearCalled = 0
            private var seekToCalled = 0

            override fun checkBuildCalled(times: Int) = assertEquals(times, buildCalled)

            override fun checkPlayPauseCalled(times: Int) = assertEquals(times, playPauseCalled)

            override fun checkRewindCalled(times: Int) = assertEquals(times, rewindCalled)

            override fun checkClearCalled(times: Int) = assertEquals(times, clearCalled)

            override fun checkSeekToCalled(times: Int) = assertEquals(times, seekToCalled)

            override fun checkConnected(connected: Boolean) = assertEquals(connected, isConnected)

            override fun onDisconnected(block: () -> Unit) {
                onDisconnectedBlock = block
            }

            override fun onConnected(block: (controller: MediaController) -> Unit) {
                onConnectedBlock = block
            }

            override fun build() {
                buildCalled++
            }

            override fun connect(create: CreatePlayerListener) {
                isConnected = true
                val controller: MediaController = mock()
                onConnectedBlock?.invoke(controller)
                controller.addListener(create.listener(controller))
            }

            override fun disconnect() {
                if (isConnected) {
                    isConnected = false
                    onDisconnectedBlock?.invoke()
                }
            }

            override fun playPause() {
                playPauseCalled++
                controller?.run {
                    if (playWhenReady) pause() else play()
                }
            }

            override fun rewind() {
                rewindCalled++
                controller?.run {
                    seekToPrevious()
                    play()
                }
            }

            override fun clearMediaQueue() {
                clearCalled++
                controller?.run {
                    pause()
                    stop()
                    clearMediaItems()
                }
            }

            override fun seek(pos: Long) {
                seekToCalled++
                controller?.seekTo(pos)
            }
        }
    }
}