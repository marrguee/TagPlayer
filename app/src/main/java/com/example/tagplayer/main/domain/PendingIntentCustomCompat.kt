package com.example.tagplayer.main.domain

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.media3.common.util.UnstableApi

@UnstableApi
interface PendingIntentCustomCompat {
    fun foregroundService(context: Context, currentAction: String) : PendingIntent

    object Base : PendingIntentCustomCompat {
        override fun foregroundService(context: Context, currentAction: String) : PendingIntent {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PendingIntent.getForegroundService(
                    context,
                    0,
                    Intent(context, TagPlayerService::class.java).apply {
                        action = currentAction
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getService(
                    context,
                    0,
                    Intent(context, TagPlayerService::class.java).apply {
                        action = currentAction
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
        }
    }
}