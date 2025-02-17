package com.example.tagplayer.tag_settings.add_tag.data

import com.example.tagplayer.tag_settings.add_tag.domain.AddTagRepository

class AddTagRepositoryImpl(
    private val cacheDatasource: AddTagDatasource
) : AddTagRepository {
    override suspend fun addTag(id: Long, title: String, color: String) {
        cacheDatasource.addTag(id, title, color)
    }
}