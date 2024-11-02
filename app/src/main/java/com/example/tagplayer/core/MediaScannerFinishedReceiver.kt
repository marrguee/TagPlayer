package com.example.tagplayer.core

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import com.example.tagplayer.core.data.ForegroundWrapper

class MediaScannerFinishedReceiver(
    private val foregroundWrapper: ForegroundWrapper
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_MEDIA_SCANNER_FINISHED) {
            val uri: Uri? = intent.data
            uri?.let {
                val type = context.contentResolver.getType(uri)

                if (type?.startsWith("audio/") == true) {
                    foregroundWrapper.fetchNewSong(uri)
                }
            }
        }
    }
}

class MediaObserver(
    handler: Handler,
    private val foregroundWrapper: ForegroundWrapper
) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
        if (uri == null) return
        if (flags == ContentResolver.NOTIFY_INSERT || flags == ContentResolver.NOTIFY_UPDATE)
            foregroundWrapper.fetchNewSong(uri)
        if (flags == ContentResolver.NOTIFY_DELETE)
            return //todo delete song
    }
}
