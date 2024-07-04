package com.example.tagplayer.core.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.Song
import kotlinx.coroutines.flow.Flow

interface AllCacheDatasource {
    class Base(
        private val database: MediaDatabase
    ) : AbstractCacheDatasource<Any, Song>(), AllCacheDatasource {

        override fun request(vararg params: Any): Flow<List<Song>> =
            database.songsDao.songs()

    }
}