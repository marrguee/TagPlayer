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
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.TagPlayerService
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaybackControlViewModel(
    private val communication: Communication<PlaybackControlState>
) : ViewModel() {
    fun observe(owner: LifecycleOwner, observer: Observer<in PlaybackControlState>){
        communication.observe(owner, observer)
    }
    @UnstableApi
    fun connectToService(context: Context){

        val sessionToken = SessionToken(
            context,
            ComponentName(context,
                TagPlayerService::class.java)
        )
        val controllerFuture =
            MediaController.Builder(context, sessionToken)
                .buildAsync()

        controllerFuture.addListener({
            val controller = controllerFuture.get()
            communication.update(
                PlaybackControlState.UpdatePlayPause(
                    controller.playWhenReady
                )
            )
            communication.update(
                PlaybackControlState.UpdateMetadata(
                    context,
                    controller.currentPosition,
                    controller.mediaMetadata
                )
            )

            viewModelScope.launch {
                while (true) {
                    communication.update(
                        PlaybackControlState.UpdateSeekbar(
                            controller.currentPosition
                        )
                    )
                    delay(1000)
                }
            }
            controller.addListener(object : Listener {

                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, reason)
                    communication.update(
                        PlaybackControlState.UpdatePlayPause(
                            controller.playWhenReady
                        )
                    )
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    communication.update(
                        PlaybackControlState.UpdateMetadata(
                            context,
                            controller.currentPosition,
                            controller.mediaMetadata
                        )
                    )
                }
            })
        }, MoreExecutors.directExecutor())
    }
}