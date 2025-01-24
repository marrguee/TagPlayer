package com.example.tagplayer.playback_control.presentation

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.TagPlayerService
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@UnstableApi
class PlaybackControlViewModel(
    private val observable: CustomObservable.All<PlaybackState>
) : ViewModel(), HandleUiStateUpdates.All<PlaybackState> {
    private lateinit var controller: MediaController
    private var job: Job? = null

    fun connectToService(context: Context) {
        val sessionToken = SessionToken(
            context, ComponentName(
                context, TagPlayerService::class.java
            )
        )
        val controllerFuture: ListenableFuture<MediaController> =
            MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture.addListener({
            controller = controllerFuture.get()
            controller.addListener(object : Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, reason)
                    observable.update(
                        PlaybackState.UpdatePlayPause(
                            playWhenReady
                        )
                    )
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    observable.update(
                        PlaybackState.UpdateMetadata(
                            context, mediaMetadata
                        )
                    )
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY && job == null) {
                        observable.update(PlaybackState.UpdateDuration(controller.duration))
                        job = viewModelScope.launch {
                            while (true) {
                                observable.update(
                                    PlaybackState.UpdatePosition(controller.currentPosition)
                                )
                                delay(100)
                            }
                        }
                    } else if (playbackState == Player.STATE_ENDED) {
                        observable.update(PlaybackState.UpdatePosition(0))
                        job?.cancel()
                        job = null
                    }
                }
            })
        }, MoreExecutors.directExecutor())
    }

    fun playPause() {
        if (controller.playWhenReady) controller.pause()
        else controller.play()
    }

    fun resetSong() {
        controller.seekToPrevious()
        controller.play()
    }

    fun seekTo(position: Long) {
        controller.seekTo(position)
    }

    override fun startGettingUpdates(observer: CustomObserver<PlaybackState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(PlaybackControlObserver.Empty)

    override fun clear() = observable.clear()
}