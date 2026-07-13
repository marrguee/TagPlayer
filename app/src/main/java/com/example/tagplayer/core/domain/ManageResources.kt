package com.example.tagplayer.core.domain

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.tagplayer.R

interface ManageResources {
    interface Strings {
        fun string(id: Int) : String
    }

    interface Notifications {
        fun notificationChannelName() : String
        fun notificationChannelId() : String
    }

    interface SongIdError {
        fun songIdError() : String
        fun retrieveIdError() : String
    }

    interface All : Strings, Notifications, SongIdError

    class Base(private val context: Context) : All {

        private val block: (Int) -> String = { ContextCompat.getString(context, it) }

        override fun notificationChannelName() = block.invoke(R.string.notification_channel_name)

        override fun notificationChannelId() = block.invoke(R.string.notification_channel_name)

        override fun songIdError() = block.invoke(R.string.song_id_error)

        override fun retrieveIdError() = block.invoke(R.string.retrieve_id_error)

        override fun string(id: Int): String = block.invoke(id)
    }
}