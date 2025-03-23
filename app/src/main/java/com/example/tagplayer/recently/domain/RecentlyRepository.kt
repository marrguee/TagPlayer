package com.example.tagplayer.recently.domain

import com.example.tagplayer.core.domain.PlayForeground

interface RecentlyRepository<H> :
    //HandleRepositoryRequest<H, Any>,
    PlayForeground {
        suspend fun recently() : List<H>
    }