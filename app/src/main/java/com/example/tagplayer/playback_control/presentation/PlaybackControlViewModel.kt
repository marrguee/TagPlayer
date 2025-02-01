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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
class PlaybackControlViewModel(
    private val observable: CustomObservable.All<PlaybackState>,
) : ViewModel(), HandleUiStateUpdates.All<PlaybackState> {
    private lateinit var controller: MediaController
    private var playerListener: Listener = object : Listener {}
    private var job: Job? = null

    fun connectService(context: Context) {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, TagPlayerService::class.java)
        )
        val controllerFuture: ListenableFuture<MediaController> =
            MediaController.Builder(context, sessionToken)
                .setListener(
                    object : MediaController.Listener {
                        override fun onDisconnected(controller: MediaController) {
                            observable.update(PlaybackState.UpdatePlayPause(false))
                            observable.update(PlaybackState.UpdateMetadata(MediaMetadata.EMPTY))
                            observable.update(PlaybackState.UpdatePosition(0))
                            observable.update(PlaybackState.UpdateTimeBarEnabled(false))
                            job?.let {
                                it.cancel()
                                job = null
                            }
                            super.onDisconnected(controller)
                        }
                })
                .buildAsync()

        controllerFuture.addListener({
            controller = controllerFuture.get()

            with(controller) {
                observable.let {
                    it.update(PlaybackState.UpdatePlayPause(playWhenReady))
                    it.update(PlaybackState.UpdateMetadata(mediaMetadata))
                    it.update(
                        PlaybackState.UpdateDuration(
                            if (playbackState == Player.STATE_READY) duration
                            else 0
                        )
                    )
                }
            }

            playerListener = object : Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, reason)
                    observable.update(PlaybackState.UpdatePlayPause(playWhenReady))
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    var currentMetadata = mediaMetadata
                    if (mediaMetadata.artist.isNullOrBlank())
                        currentMetadata = mediaMetadata.buildUpon()
                            .setArtist(METADATA_UNKNOWN_ARTIST)
                            .build()
                    observable.update(PlaybackState.UpdateMetadata(currentMetadata))
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        observable.update(PlaybackState.UpdateDuration(controller.duration))
                        observable.update(PlaybackState.UpdateTimeBarEnabled(true))
                        job = viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
                            while (true) {
                                withContext(Dispatchers.Main) {
                                    observable.update(
                                        PlaybackState.UpdatePosition(controller.currentPosition)
                                    )
                                }
                                delay(100)
                            }
                        }
                    } else {
                        job?.let {
                            it.cancel()
                            job = null
                        }
                    }
                }
            }
            controller.addListener(playerListener)
        }, MoreExecutors.directExecutor())
    }

    fun playPause() {
        if (controller.playWhenReady) controller.pause()
        else controller.play()
    }

    fun rewindSong() {
        controller.seekToPrevious()
        controller.play()
    }

    fun seekTo(position: Long) =
        controller.seekTo(position)

    fun disconnectService() {
        controller.removeListener(playerListener)
        playerListener = object : Listener {}
    }

    override fun startGettingUpdates(observer: CustomObserver<PlaybackState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() =
        observable.updateObserver(PlaybackControlObserver.Empty)

    override fun clear() = observable.clear()

    companion object {
        private const val METADATA_UNKNOWN_ARTIST = "Unknown Artist"
    }
}