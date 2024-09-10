package com.example.tagplayer.home.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import kotlinx.coroutines.flow.Flow

interface HomeRepository<T> :
    //HandleRepositoryRequest<T, Any>,
    PlaySongForeground,
    ScanSongsForeground {
        fun library() : Flow<List<T>>
        suspend fun recently() : List<T>
    }

