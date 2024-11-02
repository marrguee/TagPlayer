package com.example.tagplayer.filter_by_tags

import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag

interface TagFilterCacheDatasource {
    suspend fun tags(): List<SongTag>
    suspend fun applyFilter(selectedTags: List<Long>)

    class SharedPref(
        private val prefs: SharedPrefs.Save<List<Long>>,
        private val tagsDao: TagsDao,
    ) : TagFilterCacheDatasource {
        override suspend fun tags(): List<SongTag> {
            return tagsDao.tagsList()
        }

        override suspend fun applyFilter(selectedTags: List<Long>) {
            prefs.save(selectedTags)
        }
    }
}