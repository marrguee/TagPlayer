package com.example.tagplayer.core

import android.net.Uri
import android.os.Bundle

interface HandleMediaExtras {
    fun putMediaId(id: Long): HandleMediaExtras
    fun getMediaId(bundle: Bundle?): Long
    fun putMediaUri(uri: String): HandleMediaExtras
    fun getMediaUri(bundle: Bundle?): Uri
    fun build(): Bundle

    object Base: HandleMediaExtras {
        private const val SONG_ID_KEY = "SONG_ID_KEY"
        private const val SONG_URI_KEY = "SONG_URI_KEY"
        private val bundle: Bundle = Bundle()

        override fun putMediaId(id: Long): HandleMediaExtras {
            bundle.putLong(SONG_ID_KEY, id)
            return this
        }

        override fun getMediaId(bundle: Bundle?): Long =
            bundle?.getLong(SONG_ID_KEY)?:Long.MIN_VALUE

        override fun putMediaUri(uri: String): HandleMediaExtras {
            bundle.putString(SONG_URI_KEY, uri)
            return this
        }

        override fun getMediaUri(bundle: Bundle?): Uri {
            return Uri.parse(bundle?.getString(SONG_URI_KEY))
        }

        override fun build(): Bundle = bundle
    }
}