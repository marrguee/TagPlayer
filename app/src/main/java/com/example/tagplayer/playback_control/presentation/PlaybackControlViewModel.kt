package com.example.tagplayer.playback_control.presentation

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player.Listener
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.TagPlayerService
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaybackControlViewModel(
    private val observable: CustomObservable.All<PlaybackControlState>
) : ViewModel(), HandleUiStateUpdates.All<PlaybackControlState> {
    private lateinit var controller: MediaController

    @UnstableApi
    fun connectToService(context: Context) {
        val sessionToken = SessionToken(
            context,
            ComponentName(
                context,
                TagPlayerService::class.java
            )
        )
        val controllerFuture: ListenableFuture<MediaController> =
            MediaController.Builder(context, sessionToken)
                .buildAsync()

        controllerFuture.addListener({
            controller = controllerFuture.get()
            controller.addListener(object : Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, reason)
                    observable.update(
                        PlaybackControlState.UpdatePlayPause(
                            controller.playWhenReady
                        )
                    )
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    observable.update(
                        PlaybackControlState.UpdateMetadata(
                            context,
                            controller.mediaMetadata
                        )
                    )
                }
            })
        }, MoreExecutors.directExecutor())
    }

    fun playPause() {
        if (controller.playWhenReady)
            controller.pause()
        else
            controller.play()
    }

    fun resetSong() {
        controller.seekToPrevious()
    }

    override fun startGettingUpdates(observer: CustomObserver<PlaybackControlState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(PlaybackControlObserver.Empty)

    override fun clear() = observable.clear()
}