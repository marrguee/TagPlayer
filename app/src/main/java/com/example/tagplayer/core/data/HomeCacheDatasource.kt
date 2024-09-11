package com.example.tagplayer.core.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.filter_by_tags.SharedPrefs
import kotlinx.coroutines.flow.Flow

interface HomeCacheDatasource {
    fun library(): Flow<List<Song>>
    suspend fun recently() : List<Song>
    suspend fun filters(): List<Long>
    suspend fun filtered(tags: List<Long>): List<Song>

    class Base(
        private val database: MediaDatabase,
        private val prefs: SharedPrefs.Read<List<Long>>,
    ) : HomeCacheDatasource {

        override fun library(): Flow<List<Song>> =
            database.songsDao.library()

        override suspend fun recently(): List<Song> =
            database.lastPlayed.recently()

        override suspend fun filters(): List<Long> {
            return prefs.read()
        }

        override suspend fun filtered(tags: List<Long>): List<Song> {
            val list = database.songsDao.songsByTagsId(tags)
            return list
        }

    }
}