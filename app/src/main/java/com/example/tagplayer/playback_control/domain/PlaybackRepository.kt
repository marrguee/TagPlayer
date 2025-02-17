package com.example.tagplayer.playback_control.domain

import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.home.data.HandleMediaResult
import kotlinx.coroutines.flow.Flow


interface PlaybackRepository<T> {
    fun tags(songId: Long): Flow<List<T>>
    suspend fun deleteSong(songId: Long): HandleMediaResult
}