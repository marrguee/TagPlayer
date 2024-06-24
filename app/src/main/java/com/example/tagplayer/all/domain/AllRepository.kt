package com.example.tagplayer.all.domain

import kotlinx.coroutines.flow.Flow

interface AllRepository<T> {
    fun songsFlow() : Flow<List<T>>
    fun searchSongsForeground()
    fun playSongForeground(id: Long)
}