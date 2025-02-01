package com.example.tagplayer.core

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList

@UnstableApi
class CustomMediaNotificationProvider(context: Context) :
    DefaultMediaNotificationProvider(context) {
    override fun addNotificationActions(
        mediaSession: MediaSession,
        mediaButtons: ImmutableList<CommandButton>,
        builder: NotificationCompat.Builder,
        actionFactory: MediaNotification.ActionFactory
    ): IntArray {
        val defaultRewindCommandButton = mediaButtons.getOrNull(0)
        val defaultPlayPauseCommandButton = mediaButtons.getOrNull(1)
        val stopCommandButton = mediaButtons.getOrNull(2)
        val notificationMediaButtons =
            if (defaultRewindCommandButton != null &&
                defaultPlayPauseCommandButton != null &&
                stopCommandButton != null) {
                ImmutableList.builder<CommandButton>().apply {
                    add(defaultPlayPauseCommandButton)
                    add(defaultRewindCommandButton)
                    add(stopCommandButton)
                }.build()
            } else {
                mediaButtons
            }
        return super.addNotificationActions(
            mediaSession,
            notificationMediaButtons,
            builder,
            actionFactory
        )
    }

    override fun getNotificationContentText(metadata: MediaMetadata): CharSequence? =
        if (metadata.artist.isNullOrBlank()) METADATA_UNKNOWN_ARTIST else metadata.artist

    companion object {
        private const val METADATA_UNKNOWN_ARTIST = "Unknown Artist"
    }
}