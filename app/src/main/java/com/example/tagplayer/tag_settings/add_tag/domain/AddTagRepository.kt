package com.example.tagplayer.tag_settings.add_tag.domain

interface AddTagRepository {
    suspend fun addTag(id: Long, title: String, color: String)
}
