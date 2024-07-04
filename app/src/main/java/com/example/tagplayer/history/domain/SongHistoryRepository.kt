package com.example.tagplayer.history.domain

import com.example.tagplayer.core.data.HandleRepositoryRequest
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.domain.UpdateSongHistory
import kotlinx.coroutines.flow.Flow

interface SongHistoryRepository<H> :
    HandleRepositoryRequest<H, Any>,
    PlaySongForeground