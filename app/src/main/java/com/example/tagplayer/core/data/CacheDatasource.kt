package com.example.tagplayer.core.data

import com.example.tagplayer.core.domain.UpdateHistory
import kotlinx.coroutines.flow.Flow

interface CacheDatasource : UpdateHistory {
    fun songs() : Flow<List<SongData>>
    fun history() : Flow<List<SongLastPlayedCrossRef>>
    suspend fun uri(id: Long) : String

    class Base(
        private val database: MediaDatabase
    ) : CacheDatasource {
        override fun songs(): Flow<List<SongData>> = database.songsDao.songs()
        override fun history(): Flow<List<SongLastPlayedCrossRef>> {
            return try { database.lastPlayed.playedHistory() }
            catch (e: Exception){
                throw e
            }
        }

        override suspend fun uri(id: Long) = database.songsDao.uriById(id)
        override suspend fun updateHistory(lastPlayed: LastPlayed) {
            database.lastPlayed.wasPlayed(lastPlayed)
        }
    }
}