package com.example.tagplayer.search.domain

import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.core.data.HandleRepositoryRequest
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.main.presentation.SongUi


interface SearchRepository<T, E> : //todo 1 generic
    //HandleRepositoryRequest<E, String>,
    SearchHistory<T>,
    UpdateSearchHistory<SearchHistoryTable>,
    PlaySongForeground {
        suspend fun findSongsByTitle(songTitle: String) : List<SongDomain>
    }