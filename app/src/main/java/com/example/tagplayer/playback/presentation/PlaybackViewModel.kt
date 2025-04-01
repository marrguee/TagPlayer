package com.example.tagplayer.playback.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.session.MediaController
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.media_service.HandleMediaExtras
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.playback.domain.PlaybackInteractor
import com.example.tagplayer.playback.domain.SongDetailsResponse
import com.example.tagplayer.playback.presentation.PlaybackState.Connected
import com.example.tagplayer.playback.presentation.PlaybackState.Disconnected
import com.example.tagplayer.playback.presentation.PlaybackState.StartPlay
import com.example.tagplayer.playback.presentation.PlaybackState.StopPlay
import com.example.tagplayer.playback.presentation.PlaybackState.UpdateMetadata
import com.example.tagplayer.playback.presentation.PlaybackState.UpdatePlayPause
import com.example.tagplayer.playback.presentation.PlaybackState.UpdatePosition
import com.example.tagplayer.tags_attach.presentation.AttachTagsScreen
import kotlinx.coroutines.delay

class PlaybackViewModel(
    private val runAsync: RunAsync,
    private val observable: CustomObservable.All<PlaybackState>,
    private val interactor: PlaybackInteractor,
    private val mapper: SongDetailsResponse.Mapper,
    private val navigation: Navigation.Navigate,
    private val mediaExtras: HandleMediaExtras.Obtain,
    private val handleConnection: HandleConnection,
) : ViewModel(), HandleUiStateUpdates.All<PlaybackState>, PlayerCommands, CreatePlayerListener {
    private var songId: Long = Long.MIN_VALUE
    private val uiBlock: (SongDetailsResponse) -> Unit = { it.map(mapper, viewModelScope) }

    init {
        handleConnection.run {
            onConnected { observable.update(Connected(it)) }
            onDisconnected { observable.update(Disconnected) }
            build()
        }
    }

    fun shareSong() = runAsync.handle(viewModelScope, uiBlock) {
        interactor.share(songId)
    }

    fun deleteSong() = runAsync.handle(viewModelScope, uiBlock) {
        interactor.deleteSong(songId)
    }

    fun editTags() = navigation.update(AttachTagsScreen(songId))

    override fun playPause() = handleConnection.playPause()
    override fun rewind() = handleConnection.rewind()
    override fun seek(pos: Long) = handleConnection.seek(pos)
    override fun clearMediaQueue() = handleConnection.clearMediaQueue()

    override fun startGettingUpdates(observer: CustomObserver<PlaybackState>) = observable.run {
        updateObserver(observer)
        handleConnection.connect(this@PlaybackViewModel)
    }

    override fun stopGettingUpdates() = observable.run {
        handleConnection.disconnect()
        updateObserver(PlaybackObserver.Empty)
    }

    override fun clear() = observable.clear()

    override fun listener(controller: MediaController): Listener = object : Listener {
        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            super.onPlayWhenReadyChanged(playWhenReady, reason)
            observable.update(UpdatePlayPause(playWhenReady))

            if (playWhenReady) runAsync.handleMultiply(
                viewModelScope,
                controller.playWhenReady,
                { observable.update(UpdatePosition(controller.currentPosition)) }) {
                    delay(100)
                }
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onMediaMetadataChanged(mediaMetadata)
            songId = mediaExtras.getMediaId(mediaMetadata.extras)
            interactor.tags(songId).map(mapper, viewModelScope)
            observable.update(UpdateMetadata(mediaMetadata))
        }

        @SuppressLint("SwitchIntDef")
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> observable.update(StopPlay)
                Player.STATE_READY -> observable.update(StartPlay(controller.duration))
            }
        }
    }
}