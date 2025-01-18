package com.example.tagplayer.tag_settings.domain

interface RemoveTag<T> {
    suspend fun removeTag(id: T)
}