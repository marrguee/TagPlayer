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

    fun notificationChannelName(): String
    fun notificationChannelId(): String

    class Base(private val context: Context) : ManageResources {
        override fun notificationChannelName() =
            context.getString(R.string.notification_channel_name)

        override fun notificationChannelId() =
            context.getString(R.string.notification_channel_name)

    }
}