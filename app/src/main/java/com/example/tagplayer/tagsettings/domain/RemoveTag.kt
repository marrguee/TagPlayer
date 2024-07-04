package com.example.tagplayer.tagsettings.domain

interface RemoveTag<T> {
    suspend fun removeTag(id: T)
}