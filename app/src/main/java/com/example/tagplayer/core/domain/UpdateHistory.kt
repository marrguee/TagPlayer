package com.example.tagplayer.core.domain

import com.example.tagplayer.core.data.LastPlayed

interface UpdateHistory {
    suspend fun updateHistory(lastPlayed: LastPlayed)
}