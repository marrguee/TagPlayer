package com.example.tagplayer.core

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT
import androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ControllerInfo
import androidx.media3.session.MediaSessionService
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.domain.ProvideLastPlayedDao
import com.example.tagplayer.core.domain.ProvideSongsDao
import com.example.tagplayer.main.domain.ManageNotification
import com.example.tagplayer.main.domain.ShowNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@UnstableApi
class TagPlayerService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession
    private lateinit var manageNotification: ManageNotification
    private lateinit var showNotification: ShowNotification
    private lateinit var notificationManager: NotificationManager
    private lateinit var coroutineScope: CoroutineScope
    private var firstStart = true

    override fun onCreate() {
        super.onCreate()
        coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()

        mediaSession =
            MediaSession.Builder(this, player)
                .setCallback(TagPlayerCallback())
                //.setSessionActivity() - open activity when tap
                .build()

        manageNotification = ManageNotification.Base(
            this,
            mediaSession,
            (application as ManageResources.Provide).manageRecourses()
        )

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        showNotification = ShowNotification.Base(
            this,
            notificationManager,
            manageNotification
        )

        manageNotification.prepareChannel(notificationManager)

        mediaSession.player.repeatMode = Player.REPEAT_MODE_ONE

        mediaSession.player.addListener(object : Player.Listener {

            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                if (events.containsAny(
                        Player.EVENT_PLAY_WHEN_READY_CHANGED,
                        Player.EVENT_MEDIA_METADATA_CHANGED))
                    showNotification.show()
            }
        })

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val player = mediaSession.player

        intent?.let {
            when(it.action) {

                START_SERVICE ->
                    coroutineScope.launch {
                        val songId = it.getLongExtra(ID_KEY, -1)

                        val songsDao =
                            (application as ProvideSongsDao).songsDao()
                        val lastPlayedDao =
                            (application as ProvideLastPlayedDao).lastPlayedDao()

                        val uri = songsDao.uriById(songId)
                        val requestedSong = MediaItem.fromUri(uri)

                        withContext(Dispatchers.Main.immediate) {
                            with(player) {
                                if(currentMediaItem == requestedSong)
                                    return@withContext
                                if(mediaItemCount > 0) {
                                    stop()
                                    clearMediaItems()
                                }
                                addMediaItem(requestedSong)
                                lastPlayedDao.wasPlayed(LastPlayed(songId, Date()))
                                prepare()
                                play()
                            }
                        }
                    }

                STOP_SERVICE -> stopSelf()

                PLAY_ACTION -> player.play()

                PAUSE_ACTION -> player.pause()

                RESTART_ACTION -> player.seekToPrevious()
                else -> {}
            }
        }

        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: ControllerInfo) = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        with(mediaSession.player){
            if (!playWhenReady
                || mediaItemCount == 0
                || playbackState == Player.STATE_ENDED) {
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        mediaSession.run {
            player.release()
            release()
        }
        super.onDestroy()
    }

    private inner class TagPlayerCallback : MediaSession.Callback {
        override fun onConnect(
            session: MediaSession,
            controller: ControllerInfo
        ): MediaSession.ConnectionResult {
            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailablePlayerCommands(
                    MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon()
                        .remove(COMMAND_SEEK_TO_NEXT)
                        .remove(COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                        .build()
                )
                .build()
        }
    }

    companion object {
        const val ID_KEY = "ID_KEY"
        const val PLAY_ACTION = "PLAY_ACTION"
        const val PAUSE_ACTION = "PAUSE_ACTION"
        const val START_SERVICE = "START_SERVICE"
        const val STOP_SERVICE = "STOP_SERVICE"
        const val RESTART_ACTION = "RESTART_ACTION"
        const val ACTION_SERVICE_STARTED = "ACTION_SERVICE_STARTED"
        const val ACTION_SERVICE_STOPPED = "ACTION_SERVICE_STOPPED"
    }
}

