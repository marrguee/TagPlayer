package com.example.tagplayer.search.data

import com.example.tagplayer.core.data.AbstractCacheDatasource
import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.search.domain.SearchHistory
import com.example.tagplayer.search.domain.UpdateSearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchCacheDatasource :
    UpdateSearchHistory<SearchHistoryTable>,
    SearchHistory<SearchHistoryTable>
{
    class Base(
        private val database: MediaDatabase
    ) : AbstractCacheDatasource<String, Song>(), SearchCacheDatasource {
        override suspend fun updateSearch(searchHistory: SearchHistoryTable) =
            database.searchHistoryDao.updateSearch(searchHistory)
        override suspend fun searchHistory(): List<SearchHistoryTable> =
            database.searchHistoryDao.searchHistory()

        override fun request(vararg params: String): Flow<List<Song>> =
            database.songsDao.searchSongs(params[0])

    }

}