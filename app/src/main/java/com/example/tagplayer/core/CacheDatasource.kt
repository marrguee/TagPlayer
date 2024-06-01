package com.example.tagplayer.core

import com.example.tagplayer.all.data.ExtractMedia
import com.example.tagplayer.all.data.TrackData

interface CacheDatasource {
    suspend fun tracks() : List<TrackData>

    class Base(
        private val extractMedia: ExtractMedia
    ) : CacheDatasource {
        override suspend fun tracks() = extractMedia.media()
    }
}