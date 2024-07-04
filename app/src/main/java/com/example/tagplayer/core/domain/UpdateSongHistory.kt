package com.example.tagplayer.core.domain

import com.example.tagplayer.core.data.database.models.LastPlayed

interface UpdateSongHistory<T> {
    suspend fun songToHistory(lastPlayed: T)
}