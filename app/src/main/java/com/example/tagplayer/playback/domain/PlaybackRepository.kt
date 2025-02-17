package com.example.tagplayer.playback.domain

import com.example.tagplayer.home.data.HandleMediaResult
import kotlinx.coroutines.flow.Flow


interface PlaybackRepository<T> {
    fun tags(songId: Long): Flow<List<T>>
    suspend fun deleteSong(songId: Long): HandleMediaResult
}