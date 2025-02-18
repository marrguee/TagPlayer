package com.example.tagplayer.playback.data

import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.home.data.HandleMediaResult
import com.example.tagplayer.home.data.HandleMediaStore
import com.example.tagplayer.playback.domain.HandleSongDetails
import kotlinx.coroutines.flow.Flow

interface PlaybackCacheDatasource : HandleSongDetails.Mutable<SongTag, HandleMediaResult> {
    class Base(
        private val handleMediaStore: HandleMediaStore,
        private val tagsDao: TagsDao,
        private val songsDao: SongsDao,
    ): PlaybackCacheDatasource {

        override fun tags(id: Long): Flow<List<SongTag>> = tagsDao.ownedTags(id)

        override suspend fun uri(id: Long): String = songsDao.uriById(id)

        override suspend fun deleteSong(songId: Long): HandleMediaResult =
            handleMediaStore.deleteSong(songId)
    }
}