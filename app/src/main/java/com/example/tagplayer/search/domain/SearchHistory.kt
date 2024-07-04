package com.example.tagplayer.search.domain

interface SearchHistory<T> {
    suspend fun searchHistory() : List<T>
}