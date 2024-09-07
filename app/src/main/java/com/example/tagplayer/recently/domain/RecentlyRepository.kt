package com.example.tagplayer.recently.domain

import com.example.tagplayer.core.domain.PlaySongForeground

interface RecentlyRepository<H> :
    //HandleRepositoryRequest<H, Any>,
    PlaySongForeground {
        suspend fun recently() : List<H>
    }