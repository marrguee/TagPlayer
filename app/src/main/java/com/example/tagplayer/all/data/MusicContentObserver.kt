package com.example.tagplayer.all.data

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler

class MusicContentObserver(
    handler: Handler,
    private val onChange: () -> Unit
) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        onChange()
    }
}
