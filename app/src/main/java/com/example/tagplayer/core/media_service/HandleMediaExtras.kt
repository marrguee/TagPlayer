package com.example.tagplayer.core.media_service

import android.net.Uri
import android.os.Bundle

interface HandleMediaExtras {

    interface Obtain {
        fun getMediaId(bundle: Bundle?): Long
        fun getMediaUri(bundle: Bundle?): Uri
    }

    interface Put {
        fun putMediaId(id: Long): Put
        fun putMediaUri(uri: String): Put
        fun build(): Bundle
    }

    interface Mutable : Obtain, Put

    abstract class Base {
        protected val idKey = "SONG_ID_KEY"
        protected val uriKey = "SONG_URI_KEY"
    }

    class Read : Base(), Obtain {
        override fun getMediaId(bundle: Bundle?): Long =
            bundle?.getLong(idKey)?:Long.MIN_VALUE


        override fun getMediaUri(bundle: Bundle?): Uri {
            return Uri.parse(bundle?.getString(uriKey))
        }
    }

    class Save : Base(), Put {
        private val bundle: Bundle = Bundle()

        override fun putMediaId(id: Long): Put {
            bundle.putLong(idKey, id)
            return this
        }

        override fun putMediaUri(uri: String): Put {
            bundle.putString(uriKey, uri)
            return this
        }

        override fun build(): Bundle = bundle
    }
}