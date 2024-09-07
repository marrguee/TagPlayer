package com.example.tagplayer.tagsettings.add_tag

interface AddTagRepository {
    suspend fun addTag(id: Long, title: String, color: String)
}
