package com.example.tagplayer.core.domain

import com.example.tagplayer.core.data.database.dao.TagDao

interface ProvideTagDao {
    fun tagDao() : TagDao
}