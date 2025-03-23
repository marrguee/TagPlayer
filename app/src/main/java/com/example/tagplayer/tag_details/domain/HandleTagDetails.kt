package com.example.tagplayer.tag_details.domain

interface HandleTagDetails<T> {
    suspend fun tag(id: Long): T
    suspend fun add(id: Long, title: String, color: String)
}