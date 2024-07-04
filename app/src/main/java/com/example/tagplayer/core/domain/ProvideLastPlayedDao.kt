package com.example.tagplayer.core.domain

import com.example.tagplayer.core.data.database.dao.LastPlayedDao

interface ProvideLastPlayedDao {
    fun lastPlayedDao() : LastPlayedDao
}