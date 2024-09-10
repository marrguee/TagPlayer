package com.example.tagplayer.tagsettings.data

import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tagsettings.domain.AddTag
import com.example.tagplayer.tagsettings.domain.RemoveTag
import kotlinx.coroutines.flow.Flow

interface TagSettingsCacheDatasource :
    RemoveTag<Long>,
    AddTag<SongTag>
{
    fun tags() : Flow<List<SongTag>>

    class Base(
        private val database: MediaDatabase
    ) :
        //AbstractCacheDatasource<Any, SongTag>(),
        TagSettingsCacheDatasource {
        override fun tags(): Flow<List<SongTag>> {
            return database.tagsDao.tags()
        }

        override suspend fun removeTag(id: Long) =
            database.tagsDao.removeTag(id)

        override suspend fun addTag(tag: SongTag) =
            database.tagsDao.addTag(tag)

    }
}

