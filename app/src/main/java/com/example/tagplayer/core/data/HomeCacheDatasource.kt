package com.example.tagplayer.core.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.Song
import kotlinx.coroutines.flow.Flow

interface HomeCacheDatasource {
    fun library(): Flow<List<Song>>
    suspend fun recently() : List<Song>

    class Base(
        private val database: MediaDatabase
    ) :
        //AbstractCacheDatasource<Any, Song>(),
        HomeCacheDatasource {

        override fun library(): Flow<List<Song>> =
            database.songsDao.library()

        override suspend fun recently(): List<Song> =
            database.lastPlayed.recently()

    }
}