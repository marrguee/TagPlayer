package com.example.tagplayer.tag_settings.data

import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tag_settings.domain.ModifyTags
import kotlinx.coroutines.flow.Flow

interface TagSettingsCacheDatasource : ModifyTags<SongTag> {

    class Base(private val tagsDao: TagsDao) : TagSettingsCacheDatasource {

        override fun tags(): Flow<List<SongTag>> = tagsDao.tags()
        override suspend fun remove(id: Long) = tagsDao.removeTag(id)
    }
}

