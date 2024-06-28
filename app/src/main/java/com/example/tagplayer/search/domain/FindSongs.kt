package com.example.tagplayer.search.domain

interface FindSongs {
    suspend fun findSongs(query: String)
}