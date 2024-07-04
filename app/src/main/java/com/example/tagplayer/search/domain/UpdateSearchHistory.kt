package com.example.tagplayer.search.domain

interface UpdateSearchHistory<T> {
    suspend fun updateSearch(searchHistoryTable: T)
}