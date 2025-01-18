package com.example.tagplayer.tag_settings.add_tag

interface AddTagRepository {
    suspend fun addTag(id: Long, title: String, color: String)
}
