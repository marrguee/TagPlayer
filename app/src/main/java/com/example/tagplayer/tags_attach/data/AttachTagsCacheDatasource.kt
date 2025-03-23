package com.example.tagplayer.tags_attach.data

import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tags_attach.domain.HandleTags
import kotlinx.coroutines.flow.Flow

interface AttachTagsCacheDatasource : HandleTags.All<SongTag> {

    class Base(
        private val tagsDao: TagsDao,
        private val songsDao: SongsDao,
    ) : AttachTagsCacheDatasource {

        override fun all(songId: Long): Flow<List<SongTag>> = tagsDao.tagsWithoutOwned(songId)

        override fun owned(songId: Long): Flow<List<SongTag>> = tagsDao.ownedTags(songId)

        override suspend fun add(songId: Long, tagId: Long) =
            songsDao.attachTag(songId, tagId)

        override suspend fun remove(songId: Long, tagId: Long) =
            songsDao.detachTag(songId, tagId)
    }
}