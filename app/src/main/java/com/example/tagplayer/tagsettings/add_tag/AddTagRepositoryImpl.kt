package com.example.tagplayer.tagsettings.add_tag

class AddTagRepositoryImpl(
    private val cacheDatasource: AddTagDatasource
) : AddTagRepository {
    override suspend fun addTag(id: Long, title: String, color: String) {
        cacheDatasource.addTag(id, title, color)
    }
}