package com.example.tagplayer.all.domain

import com.example.tagplayer.core.data.HandleRepositoryRequest
import com.example.tagplayer.core.domain.PlaySongForeground

interface AllRepository<T> :
    HandleRepositoryRequest<T, Any>,
    PlaySongForeground,
    ScanSongsForeground

