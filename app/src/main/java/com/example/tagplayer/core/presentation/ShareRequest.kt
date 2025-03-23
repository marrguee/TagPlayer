package com.example.tagplayer.core.presentation

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity

interface ShareRequest {
    fun share(uri: Uri)

    class Base(
        private val context: Context
    ) : ShareRequest {
        override fun share(uri: Uri) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                setType("audio/*")
                putExtra(Intent.EXTRA_STREAM, uri)
                clipData = ClipData.newUri(context.contentResolver, "Song", uri)
                setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(context, Intent.createChooser(intent, "Share"), null)
        }
    }
}