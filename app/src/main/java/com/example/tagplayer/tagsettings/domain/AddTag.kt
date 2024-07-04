package com.example.tagplayer.tagsettings.domain

interface AddTag<T> {
    suspend fun addTag(tag: T)
}