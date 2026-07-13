package com.example.tagplayer.core.media_service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT
import androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ConnectionResult
import androidx.media3.session.MediaSession.ControllerInfo
import androidx.media3.session.MediaSessionService
import com.example.tagplayer.core.data.database.dao.LastPlayedDao
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.main.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.util.Date

class MediaService : MediaSessionService() {
    private val songsDao: SongsDao by inject()
    private val lastPlayedDao: LastPlayedDao by inject()

    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var mediaSession: MediaSession? = null

    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        setMediaNotificationProvider(CustomMediaNotificationProvider(this))
        mediaSession = MediaSession.Builder(this,
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
        .build()

        mediaSession?.player?.repeatMode = Player.REPEAT_MODE_ONE
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            if (it.action == START_PLAYBACK) {
                coroutineScope.launch {
                    val songId = it.getLongExtra(MEDIA_ID_KEY, -1)

                    val uri = songsDao.uriById(songId)
                    val title = songsDao.titleById(songId)
                    val requestedSong = MediaItem.fromUri(uri).buildUpon()
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setExtras(
                                    HandleMediaExtras.Save()
                                        .putMediaId(songId)
                                        .putMediaUri(uri)
                                        .build()
                                )
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
            }
        }

        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: ControllerInfo): MediaSession? = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession?.player?.run {
            if (playWhenReady) pause()
        }
        stopSelf()
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        coroutineScope.cancel()
        mediaSession = null
        super.onDestroy()
    }

    companion object {
        private const val MEDIA_ID_KEY = "MEDIA_ID_KEY"
        private const val START_PLAYBACK = "START_PLAYBACK"

        fun startIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, MediaService::class.java)
            intent.action = START_PLAYBACK
            intent.putExtra(MEDIA_ID_KEY, id)
            return intent
        }
    }

    @UnstableApi
    private inner class TagPlayerCallback : MediaSession.Callback {
        override fun onConnect(
            session: MediaSession,
            controller: ControllerInfo
        ): ConnectionResult {
            return ConnectionResult.AcceptedResultBuilder(session)
                .setAvailablePlayerCommands(
                    ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
                        .remove(COMMAND_SEEK_TO_NEXT)
                        .remove(COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                        .build()
                )
                .build()
        }
    }
}