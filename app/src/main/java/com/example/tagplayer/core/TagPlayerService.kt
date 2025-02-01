package com.example.tagplayer.core

import android.app.PendingIntent
import android.content.Intent
import android.media.session.PlaybackState
import android.os.Build
import android.os.Bundle
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT
import androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ConnectionResult
import androidx.media3.session.MediaSession.ControllerInfo
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.example.tagplayer.R
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.core.domain.ProvideLastPlayedDao
import com.example.tagplayer.core.domain.ProvideSongsDao
import com.example.tagplayer.main.presentation.MainActivity
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@UnstableApi
class TagPlayerService : MediaSessionService() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(
                this,
        ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            .build()
        )
        .setCallback(TagPlayerCallback())
        .setSessionActivity(
            PendingIntent.getActivity(
                this,
                0,
                Intent(applicationContext, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        .setCustomLayout(
            ImmutableList.of(
                CommandButton.Builder()
                    .setDisplayName(STOP_SERVICE)
                    .setIconResId(R.drawable.ic_stop)
                    .setSessionCommand(SessionCommand(STOP_SERVICE, Bundle()))
                    .build()
            )
        )
        .build()

        setMediaNotificationProvider(CustomMediaNotificationProvider(this))
        mediaSession?.player?.repeatMode = Player.REPEAT_MODE_ONE
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        intent?.let {
            when (it.action) {
                START_PLAYBACK -> coroutineScope.launch {
                    val songId = it.getLongExtra(MEDIA_ID_KEY, -1)

                    val songsDao =
                        (application as ProvideSongsDao).songsDao()
                    val lastPlayedDao =
                        (application as ProvideLastPlayedDao).lastPlayedDao()

                    val uri = songsDao.uriById(songId)
                    val title = songsDao.titleById(songId)
                    val requestedSong = MediaItem.fromUri(uri).buildUpon()
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setTitle(title)
                                .build()
                        )
                        .build()

                    withContext(Dispatchers.Main.immediate) {
                        mediaSession?.player?.let { player ->
                            if (player.mediaItemCount > 0) {
                                player.stop()
                                player.clearMediaItems()
                            }
                            player.addMediaItem(requestedSong)
                            lastPlayedDao.wasPlayed(LastPlayed(songId, Date()))
                            player.prepare()
                            player.play()
                        }

                    }
                }
                PLAY_ACTION -> mediaSession?.player?.play()
                PAUSE_ACTION ->  mediaSession?.player?.pause()
                REWIND_ACTION ->  mediaSession?.player?.seekToPrevious()
                else -> {}
            }
        }

        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: ControllerInfo): MediaSession? = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player
        if (player!!.playWhenReady) {
            player.pause()
        }
        stopSelf()
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        coroutineScope.cancel()
        mediaSession = null
        super.onDestroy()
    }

    companion object {
        const val MEDIA_ID_KEY = "MEDIA_ID_KEY"
        const val START_PLAYBACK = "START_PLAYBACK"
        const val PLAY_ACTION = "PLAY_ACTION"
        const val PAUSE_ACTION = "PAUSE_ACTION"
        const val STOP_SERVICE = "STOP_SERVICE"
        const val REWIND_ACTION = "REWIND_ACTION"
    }

    private inner class TagPlayerCallback : MediaSession.Callback {

        override fun onConnect(
            session: MediaSession,
            controller: ControllerInfo
        ): ConnectionResult {
            val sessionCommands = ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                .add(SessionCommand(STOP_SERVICE, Bundle.EMPTY))
                .build()
            return ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands)
                .setAvailablePlayerCommands(
                    ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
                        .remove(COMMAND_SEEK_TO_NEXT)
                        .remove(COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                        .build()
                )
                .build()
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            if (customCommand.customAction == STOP_SERVICE) {
                session.run {
                    player.stop()
                    player.release()
                    release()
                }
                stopService(Intent(applicationContext, TagPlayerService::class.java))
            }
            PlaybackState.STATE_NONE
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }
    }
}

