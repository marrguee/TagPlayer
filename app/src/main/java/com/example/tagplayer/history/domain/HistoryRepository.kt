package com.example.tagplayer.history.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.domain.UpdateHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository<H> : PlaySongForeground, UpdateHistory {
    fun playedHistory() : Flow<List<H>>
}