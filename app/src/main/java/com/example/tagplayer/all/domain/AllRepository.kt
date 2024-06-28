package com.example.tagplayer.all.domain

import com.example.tagplayer.core.domain.UpdateSongHistory
import kotlinx.coroutines.flow.Flow

interface AllRepository<T> : UpdateSongHistory {
    fun songsFlow() : Flow<List<T>>
    fun searchSongsForeground()
    fun playSongForeground(id: Long)
}