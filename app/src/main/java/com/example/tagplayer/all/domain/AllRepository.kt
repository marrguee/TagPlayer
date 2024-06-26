package com.example.tagplayer.all.domain

import com.example.tagplayer.core.domain.UpdateHistory
import kotlinx.coroutines.flow.Flow

interface AllRepository<T> : UpdateHistory {
    fun songsFlow() : Flow<List<T>>
    fun searchSongsForeground()
    fun playSongForeground(id: Long)
}