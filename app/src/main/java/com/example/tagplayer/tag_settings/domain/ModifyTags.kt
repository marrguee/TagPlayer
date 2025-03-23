package com.example.tagplayer.tag_settings.domain

import kotlinx.coroutines.flow.Flow

interface ModifyTags<T> {
    fun tags(): Flow<List<T>>
    suspend fun remove(id: Long)
}