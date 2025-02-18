package com.example.tagplayer.filter.domain

import kotlinx.coroutines.flow.Flow

interface ModifyFilters<T> {
    fun tags(): Flow<List<T>>
    suspend fun reset()
    suspend fun save(filter: Pair<Long, Boolean>)
}