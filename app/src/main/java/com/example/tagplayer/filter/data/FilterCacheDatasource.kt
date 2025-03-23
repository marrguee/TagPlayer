package com.example.tagplayer.filter.data

import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.filter.domain.ModifyFilters
import kotlinx.coroutines.flow.Flow

interface FilterCacheDatasource : ModifyFilters<SongTag> {
    class Base(
        private val tagsDao: TagsDao,
    ) : FilterCacheDatasource {
        override fun tags(): Flow<List<SongTag>> = tagsDao.tags()

        override suspend fun save(filter: Pair<Long, Boolean>) =
            tagsDao.updateSelected(filter.first, filter.second)

        override suspend fun reset() = tagsDao.clearSelected()
    }
}