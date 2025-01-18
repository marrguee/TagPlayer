package com.example.tagplayer.home.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.home.domain.ScanSongsForeground
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface HomeCacheDatasource : ScanSongsForeground {
    fun library(): Flow<List<Song>>
    suspend fun filters(): List<Long>
    suspend fun filtered(tags: List<Long>): Flow<List<Song>>

    class Base(
        private val database: MediaDatabase,
        private val foregroundWrapper: ForegroundWrapper,
        private val songFilterPrefs: SharedPrefs.Read<List<Long>>
    ) : HomeCacheDatasource {

        override fun library(): Flow<List<Song>> =
            database.songsDao.library()

//        override suspend fun recently(): List<Song> =
//            database.lastPlayed.recently()

        override suspend fun filters(): List<Long> {
            return songFilterPrefs.read()
        }

        override suspend fun filtered(tags: List<Long>): Flow<List<Song>> =
            database.songsDao.songsByTagsId(tags).map { it.toSet() }.map { it.toList() }

        override fun scan() {
            foregroundWrapper.scanMedia()
        }

    }
}