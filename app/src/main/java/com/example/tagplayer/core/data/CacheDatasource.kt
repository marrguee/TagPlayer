package com.example.tagplayer.core.data

import kotlinx.coroutines.flow.Flow

interface CacheDatasource {
    fun songs() : Flow<List<SongData>>
    suspend fun uri(id: Long) : String

    class Base(
        private val database: MediaDatabase
    ) : CacheDatasource {
        override fun songs(): Flow<List<SongData>> = database.songsDao.songs()
        override suspend fun uri(id: Long) = database.songsDao.uriById(id)
    }
}