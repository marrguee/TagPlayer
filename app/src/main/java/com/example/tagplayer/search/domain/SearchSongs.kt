package com.example.tagplayer.search.domain

interface SearchSongs<T> {
    suspend fun search(query: String) : List<T>
}