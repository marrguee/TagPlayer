package com.example.tagplayer.search.domain

import com.example.tagplayer.core.domain.PlaySongForeground

interface SearchRepository<T> :
    PlaySongForeground {
        suspend fun findSongsByTitle(songTitle: String) : List<T>
    }