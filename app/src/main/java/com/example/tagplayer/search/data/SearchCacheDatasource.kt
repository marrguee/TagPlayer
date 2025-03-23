package com.example.tagplayer.search.data

import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.search.domain.SearchSongs

interface SearchCacheDatasource : SearchSongs<Song> {

    class Base(private val songsDao: SongsDao) : SearchCacheDatasource {
        override suspend fun search(query: String): List<Song> = songsDao.search(query)
    }
}