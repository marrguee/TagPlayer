package com.example.tagplayer.core.data

import com.example.tagplayer.core.domain.UpdateSongHistory
import com.example.tagplayer.search.data.SearchHistory
import com.example.tagplayer.search.domain.GetSearchHistory
import kotlinx.coroutines.flow.Flow

interface CacheDatasource : UpdateSongHistory, com.example.tagplayer.search.domain.UpdateHistory<SearchHistory>,GetSearchHistory<SearchHistory> {
    fun songs() : Flow<List<SongData>>
    fun history() : Flow<List<SongLastPlayedCrossRef>>
    suspend fun uri(id: Long) : String
    suspend fun findSongs(query: String) : List<SongData>

    class Base(
        private val database: MediaDatabase
    ) : CacheDatasource {
        override fun songs(): Flow<List<SongData>> =
            database.songsDao.songs()
        override fun history(): Flow<List<SongLastPlayedCrossRef>> =
            database.lastPlayed.playedHistory()
        override suspend fun uri(id: Long) =
            database.songsDao.uriById(id)

        override suspend fun findSongs(query: String): List<SongData> =
            database.songsDao.findSongs(query)

        override suspend fun songToHistory(lastPlayed: LastPlayed) =
            database.lastPlayed.wasPlayed(lastPlayed)
        override suspend fun updateSearch(searchHistory: SearchHistory) =
            database.searchHistoryDao.updateSearch(searchHistory)
        override suspend fun searchHistory(): List<SearchHistory> =
            database.searchHistoryDao.searchHistory()
    }
}