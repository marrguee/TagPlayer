package com.example.tagplayer.search.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.Song

interface SearchCacheDatasource<T>
{
    suspend fun findSongsByTitle(songTitle: String): List<T>
    class Base(
        private val database: MediaDatabase
    ) : SearchCacheDatasource<Song> {
        override suspend fun findSongsByTitle(songTitle: String): List<Song> =
            database.songsDao.searchSongs(songTitle)

    }

}