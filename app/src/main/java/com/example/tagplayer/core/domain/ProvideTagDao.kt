package com.example.tagplayer.core.domain

import com.example.tagplayer.core.data.database.dao.TagsDao

interface ProvideTagDao {
    fun tagDao() : TagsDao
}