package com.example.tagplayer.history.data

import com.example.tagplayer.core.data.AbstractCacheDatasource
import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import kotlinx.coroutines.flow.Flow

interface HistoryCacheDatasource {
    class Base(
        private val database: MediaDatabase
    ) : AbstractCacheDatasource<Any, SongLastPlayedCrossRef>(), HistoryCacheDatasource {

        override fun request(vararg params: Any): Flow<List<SongLastPlayedCrossRef>> =
            database.lastPlayed.playedHistory()
    }
}