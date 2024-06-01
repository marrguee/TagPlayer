package com.example.tagplayer.all.domain

interface AllRepository<T> {
    suspend fun tracks() : List<T>
}