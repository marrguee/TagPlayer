package com.example.tagplayer.search.domain

import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.domain.UpdateSongHistory


interface SearchRepository<T, E> : //todo 1 generic
    GetSearchHistory<T>,
    UpdateHistory<E>,
    PlaySongForeground,
    UpdateSongHistory {

        suspend fun findSongs(query: String) : List<SongDomain>
}