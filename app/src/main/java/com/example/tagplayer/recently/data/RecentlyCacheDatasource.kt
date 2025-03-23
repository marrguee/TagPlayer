package com.example.tagplayer.recently.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef

interface RecentlyCacheDatasource {
    suspend fun recently() : List<SongLastPlayedCrossRef>
    class Base(
        private val database: MediaDatabase
    ) : RecentlyCacheDatasource {

        override suspend fun recently(): List<SongLastPlayedCrossRef> =
            database.lastPlayed.recently()
    }
}