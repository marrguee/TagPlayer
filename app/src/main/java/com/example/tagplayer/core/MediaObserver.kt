package com.example.tagplayer.core

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import com.example.tagplayer.core.data.ForegroundWrapper

class MediaObserver(
    handler: Handler,
    private val foregroundWrapper: ForegroundWrapper
) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
        if (uri == null) return
        if (flags == ContentResolver.NOTIFY_INSERT || flags == ContentResolver.NOTIFY_UPDATE)
            foregroundWrapper.fetchNewSong(uri)
        if (flags == ContentResolver.NOTIFY_DELETE)
            foregroundWrapper.deleteSong(uri)
    }
}
