package com.example.tagplayer.search.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.search.domain.SearchHistory
import com.example.tagplayer.search.domain.UpdateSearchHistory

interface SearchCacheDatasource :
    UpdateSearchHistory<SearchHistoryTable>,
    SearchHistory<SearchHistoryTable>
{
    suspend fun findSongsByTitle(songTitle: String) : List<Song>
    class Base(
        private val database: MediaDatabase
    ) :
        //AbstractCacheDatasource<String, Song>(),
        SearchCacheDatasource {
        override suspend fun findSongsByTitle(songTitle: String): List<Song> =
            database.songsDao.searchSongs(songTitle)

        override suspend fun updateSearch(searchHistory: SearchHistoryTable) =
            database.searchHistoryDao.updateSearch(searchHistory)
        override suspend fun searchHistory(): List<SearchHistoryTable> =
            database.searchHistoryDao.searchHistory()

    }

}