package com.example.tagplayer.home.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import kotlinx.coroutines.flow.Flow

interface HomeRepository<T> :
    PlaySongForeground,
    ScanSongsForeground {
        fun library(sortingType: SortingType) : Flow<List<T>>
        suspend fun filters(): List<Long>
        fun filtered(tags: List<Long>, sortingType: SortingType): Flow<List<T>>
        suspend fun croppedRecently(): List<SongDomain>
    }

