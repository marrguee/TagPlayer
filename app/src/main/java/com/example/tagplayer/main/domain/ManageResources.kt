package com.example.tagplayer.main.domain

import android.content.Context
import com.example.tagplayer.R

interface ManageResources {
    interface Provide {
        fun manageRecourses() : ManageResources
    }
    fun notificationChannelName() : String
    fun notificationChannelId() : String

    class Base(private val context: Context) : ManageResources {
        override fun notificationChannelName() =
            context.getString(R.string.notification_channel_name)

        override fun notificationChannelId() =
            context.getString(R.string.notification_channel_name)

    }

}