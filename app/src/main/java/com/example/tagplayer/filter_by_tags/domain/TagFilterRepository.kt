package com.example.tagplayer.filter_by_tags.domain

interface TagFilterRepository<T> {
    suspend fun tags(): List<T>
    suspend fun applyFilter(selectedTags: List<Long>)
}