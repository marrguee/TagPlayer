package com.example.tagplayer.playback_control.data

import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.home.data.HandleMediaResult
import com.example.tagplayer.home.data.HandleMediaStore
import kotlinx.coroutines.flow.Flow

interface PlaybackCacheDatasource {
    fun tags(songId: Long): Flow<List<SongTag>>
    suspend fun deleteSong(songId: Long): HandleMediaResult

    class Base(
        private val handleMediaStore: HandleMediaStore,
        private val tagsDao: TagsDao
    ): PlaybackCacheDatasource {

        override fun tags(songId: Long): Flow<List<SongTag>> =
            tagsDao.tagsBySongId(songId)

        override suspend fun deleteSong(songId: Long): HandleMediaResult =
            handleMediaStore.deleteSong(songId)
    }
}