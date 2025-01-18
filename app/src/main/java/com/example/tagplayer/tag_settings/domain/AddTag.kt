package com.example.tagplayer.tag_settings.domain

interface AddTag<T> {
    suspend fun addTag(tag: T)
}