package com.example.tagplayer.core.domain

import com.example.tagplayer.core.data.database.dao.SongsDao

interface ProvideSongsDao {
    fun songsDao() : SongsDao
}