package com.example.tagplayer.recently.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import kotlinx.coroutines.flow.Flow

interface RecentlyCacheDatasource {
    suspend fun recently() : List<SongLastPlayedCrossRef>
    class Base(
        private val database: MediaDatabase
    ) :
        //AbstractCacheDatasource<Any, SongLastPlayedCrossRef>(),
        RecentlyCacheDatasource {

        override suspend fun recently(): List<SongLastPlayedCrossRef> =
            database.lastPlayed.fullRecently()
    }
}