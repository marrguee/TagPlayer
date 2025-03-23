package com.example.tagplayer.core.domain

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.tagplayer.R

interface ManageResources {
    interface Provide {
        fun manageRecourses(): ManageResources
    }

    fun notificationChannelName() : String
    fun notificationChannelId() : String
    fun songIdError() : String
    fun retrieveIdError() : String

    class Base(private val context: Context) : ManageResources {

        private val block: (Int) -> String = { ContextCompat.getString(context, it) }

        override fun notificationChannelName() = block.invoke(R.string.notification_channel_name)

        override fun notificationChannelId() = block.invoke(R.string.notification_channel_name)

        override fun songIdError() = block.invoke(R.string.song_id_error)

        override fun retrieveIdError() = block.invoke(R.string.retrieve_id_error)
    }
}