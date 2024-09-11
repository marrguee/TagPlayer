package com.example.tagplayer.home.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import kotlinx.coroutines.flow.Flow

interface HomeRepository<T> :
    PlaySongForeground,
    ScanSongsForeground {
        fun library() : Flow<List<T>>
        suspend fun recently() : List<T>
        suspend fun filters(): List<Long>
        suspend fun filtered(tags: List<Long>): List<T>
    }

