package com.example.tagplayer.playback.presentation

import android.content.ClipData
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.HandleMediaExtras
import com.example.tagplayer.core.TagPlayerService
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsScreen
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.playback.domain.HandleSongResult
import com.example.tagplayer.playback.domain.PlaybackControlInteractor
import com.example.tagplayer.playback.domain.TagsResult
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicLong

class PlaybackViewModel(
    private val dispatcherList: DispatcherList,
    private val observable: CustomObservable.All<PlayState>,
    private val interactor: PlaybackControlInteractor,
    private val handleSongMapper: HandleSongResult.Mapper,
    private val tagsResultMapper: TagsResult.Mapper,
    private val navigation: Navigation.Navigate,
    private val handleMediaExtras: HandleMediaExtras,
    private var playerListener: Listener = object : Listener {},
    private val songId: AtomicLong = AtomicLong(Long.MIN_VALUE),
    private var songUri: Uri = Uri.EMPTY,
) : ViewModel(), HandleUiStateUpdates.All<PlayState> {
    private lateinit var controller: MediaController
    private var job: Job? = null

    fun connectService(context: Context) {
        observable.update(PlayState.DisableMotion)
        val sessionToken = SessionToken(
            context,
            ComponentName(context, TagPlayerService::class.java)
        )
        val controllerFuture: ListenableFuture<MediaController> =
            MediaController.Builder(context, sessionToken)
                .setListener(
                    object : MediaController.Listener {
                        override fun onDisconnected(controller: MediaController) {
                            observable.update(PlayState.ChangePlayPauseEnabled(false))
                            observable.update(PlayState.UpdatePlayPause(false))
                            observable.update(PlayState.UpdateMetadata(MediaMetadata.EMPTY))
                            observable.update(PlayState.UpdatePosition(0))
                            observable.update(PlayState.UpdateTimeBarEnabled(false))
                            observable.update(PlayState.DisableMotion)
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
                    if (controller.currentMediaItem != null)
                        it.update(PlayState.EnableMotion)
                    it.update(PlayState.UpdatePlayPause(playWhenReady))
                    it.update(PlayState.UpdateMetadata(mediaMetadata))
                    it.update(
                        PlayState.UpdateDuration(
                            if (playbackState == Player.STATE_READY) duration
                            else 0
                        )
                    )
                }
            }

            playerListener = object : Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, reason)
                    observable.update(PlayState.UpdatePlayPause(playWhenReady))
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    songId.set(handleMediaExtras.getMediaId(mediaMetadata.extras))
                    songUri = handleMediaExtras.getMediaUri(mediaMetadata.extras)
                    interactor.tags(songId.get()).map(tagsResultMapper, viewModelScope)
                    observable.update(PlayState.EnableMotion)
                    observable.update(PlayState.UpdateMetadata(mediaMetadata))
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED){
                        observable.update(PlayState.ChangePlayPauseEnabled(false))
                        observable.update(PlayState.DisableMotion)
                    }
                    if (playbackState == Player.STATE_READY) {
                        observable.update(PlayState.ChangePlayPauseEnabled(true))
                        observable.update(PlayState.UpdateDuration(controller.duration))
                        observable.update(PlayState.UpdateTimeBarEnabled(true))
                        job = viewModelScope.launch(dispatcherList.io()) {
                            while (true) {
                                withContext(dispatcherList.ui()) {
                                    observable.update(
                                        PlayState.UpdatePosition(controller.currentPosition)
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
            if (songId.get() != Long.MIN_VALUE)
                interactor.tags(songId.get()).map(tagsResultMapper, viewModelScope)
        }, MoreExecutors.directExecutor())
    }

    fun playPause() {
        if (controller.currentMediaItem == null) return
        if (controller.playWhenReady) controller.pause()
        else controller.play()
    }

    fun rewindSong() {
        if (controller.currentMediaItem == null) return
        controller.seekToPrevious()
        controller.play()
    }

    fun seekTo(position: Long) {
        if (controller.currentMediaItem == null) return
        controller.seekTo(position)
    }

    fun disconnectService() {
        controller.removeListener(playerListener)
        playerListener = object : Listener {}
    }

    fun shareSong(context: Context) {
        if (songUri == Uri.EMPTY) return
        val intent = Intent(Intent.ACTION_SEND).apply {
            setType("audio/*")
            putExtra(Intent.EXTRA_STREAM, songUri)
            clipData = ClipData.newUri(context.contentResolver, "Song", songUri)
            setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(context, Intent.createChooser(intent, "Share"), null)
    }

    fun editSongTagsScreen() {
        navigation.update(EditSongTagsScreen(listOf(songId.get())))
    }

    fun clearBeforeDeleting() {
        controller.pause()
        controller.stop()
        controller.clearMediaItems()
    }

    fun deleteSong() {
        viewModelScope.launch(dispatcherList.io()) {
            val result = interactor.deleteSong(songId.get())
            withContext(dispatcherList.ui()) {
                result.map(handleSongMapper)
            }
        }
    }

    override fun startGettingUpdates(observer: CustomObserver<PlayState>) {
        observable.updateObserver(observer)
        observable.update(PlayState.StartAnimation)
    }


    override fun stopGettingUpdates() {
        observable.update(PlayState.PauseAnimation)
        observable.updateObserver(PlaybackControlObserver.Empty)
    }

    override fun clear() = observable.clear()
}