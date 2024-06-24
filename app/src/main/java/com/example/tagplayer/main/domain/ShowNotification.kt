package com.example.tagplayer.main.domain

import android.app.NotificationManager
import android.app.Service
import android.content.pm.ServiceInfo
import androidx.core.app.ServiceCompat
import androidx.media3.common.util.UnstableApi

@UnstableApi
interface ShowNotification {
    fun show()

    class Base(
        private val context: Service,
        private val notificationManager: NotificationManager,
        private val manageNotification: ManageNotification
    ) : ShowNotification {
        private var firstStart = true
        override fun show() {
            if(firstStart) {
                ServiceCompat.startForeground(
                    context,
                    NOTIFICATION_ID,
                    manageNotification.createPreloader(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
                firstStart = false
            }
            else {
                notificationManager.notify(
                    NOTIFICATION_ID,
                    manageNotification.create()
                )
            }
        }

        companion object {
            private const val NOTIFICATION_ID = 1
        }
    }
}