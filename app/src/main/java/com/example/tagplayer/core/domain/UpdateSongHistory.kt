package com.example.tagplayer.core.domain

import com.example.tagplayer.core.data.LastPlayed

interface UpdateSongHistory {
    suspend fun songToHistory(lastPlayed: LastPlayed)
}