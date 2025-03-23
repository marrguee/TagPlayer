package com.example.tagplayer.tag_details.data

import com.example.tagplayer.tag_details.domain.HandleTagDetails
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag

interface TagDetailsDatasource : HandleTagDetails<SongTag> {
    class Base(
        private val tagsDao: TagsDao
    ) : TagDetailsDatasource {

        override suspend fun add(id: Long, title: String, color: String) =
            tagsDao.addTag(SongTag(id, title, color))

        override suspend fun tag(id: Long): SongTag = tagsDao.tag(id)
    }
}
