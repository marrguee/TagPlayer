package com.example.tagplayer.search.domain

import com.example.tagplayer.search.data.SearchHistory

interface GetSearchHistory<T> {
    suspend fun searchHistory() : List<T>
}