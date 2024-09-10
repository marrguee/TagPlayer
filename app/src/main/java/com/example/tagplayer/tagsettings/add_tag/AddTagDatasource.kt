package com.example.tagplayer.tagsettings.add_tag

import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag

interface AddTagDatasource {
    suspend fun addTag(id: Long, title: String, color: String)

    class Base(
        private val tagsDao: TagsDao
    ) : AddTagDatasource {
        override suspend fun addTag(id: Long, title: String, color: String) {
            tagsDao.addTag(SongTag(title, "#000000", id))
        }
    }
}
