package com.example.tagplayer.search.domain

import com.example.tagplayer.search.data.SearchHistory

interface UpdateHistory<T> {
    suspend fun updateSearch(searchHistory: T)
}