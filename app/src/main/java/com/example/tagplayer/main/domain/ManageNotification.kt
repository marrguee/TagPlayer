package com.example.tagplayer.main.domain

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper.MediaStyle
import com.example.tagplayer.R
import com.example.tagplayer.core.TagPlayerService
import com.example.tagplayer.core.domain.ManageResources

@UnstableApi
interface ManageNotification {
        fun create(): Notification
        fun createPreloader(): Notification
        fun prepareChannel(notificationManager: NotificationManager)
        class Base(
            context: Context,
            private val mediaSession: MediaSession,
            private val manageResources: ManageResources
        ) : ManageNotification {
            private var notificationTemplate : NotificationCompat.Builder
            private var notificationBlock : (Boolean) -> NotificationCompat.Builder

            init {
                val pendingIntentCustomCompat : PendingIntentCustomCompat =
                    PendingIntentCustomCompat.Base

                val playAction = NotificationCompat.Action(
                    R.drawable.ic_play,
                    "play",
                    pendingIntentCustomCompat.foregroundService(
                        context,
                        TagPlayerService.PLAY_ACTION
                    )
                )

                val pauseAction = NotificationCompat.Action(
                    R.drawable.ic_pause,
                    "pause",
                    pendingIntentCustomCompat.foregroundService(
                        context,
                        TagPlayerService.PAUSE_ACTION
                    )
                )

                val restartAction = NotificationCompat.Action(
                    R.drawable.ic_restart_24,
                    "restart",
                    pendingIntentCustomCompat.foregroundService(
                        context,
                        TagPlayerService.RESTART_ACTION
                    )
                )

                notificationTemplate =
                    NotificationCompat.Builder(context, manageResources.notificationChannelName())
                        .setSmallIcon(R.drawable.logo_tag_24)
                        .addAction(restartAction)
                        .setStyle(
                            MediaStyle(mediaSession)
                                .setShowActionsInCompactView(0)
                        )


                notificationBlock = {
                    NotificationCompat.Builder(context, manageResources.notificationChannelName())
                        .setSmallIcon(R.drawable.logo_tag_24)
                        .addAction(restartAction)
                        .apply {
                            if (it)
                                addAction(pauseAction)
                            else
                                addAction(playAction)
                        }
                        .setStyle(
                            MediaStyle(mediaSession)
                                .setShowActionsInCompactView(0,1)
                        )
                }
            }

            override fun create(): Notification = with(mediaSession.player) {
                notificationBlock.invoke(playWhenReady)
                    .setContentTitle(mediaMetadata.title)
                    .setContentText(mediaMetadata.artist)
                    .build()
            }

            override fun createPreloader() = notificationTemplate.build()

            override fun prepareChannel(notificationManager: NotificationManager) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(
                        NotificationChannel(
                            manageResources.notificationChannelId(),
                            manageResources.notificationChannelName(),
                            NotificationManager.IMPORTANCE_LOW
                        )
                    )
                }
            }
        }
}



//                    .apply {
//                        mediaMetadata.artworkData?.let {
//                            setLargeIcon(
//                                BitmapFactory.decodeByteArray(
//                                    it,
//                                    0,
//                                    it.size
//                                )
//                            )
//                        }
//                    }