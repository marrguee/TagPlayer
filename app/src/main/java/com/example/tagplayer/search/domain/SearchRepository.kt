package com.example.tagplayer.search.domain

import com.example.tagplayer.core.data.HandleRepositoryRequest
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.core.domain.PlaySongForeground


interface SearchRepository<T, E> : //todo 1 generic
    HandleRepositoryRequest<E, String>,
    SearchHistory<T>,
    UpdateSearchHistory<SearchHistoryTable>,
    PlaySongForeground