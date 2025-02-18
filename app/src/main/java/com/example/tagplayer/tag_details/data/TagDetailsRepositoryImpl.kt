package com.example.tagplayer.tag_details.data

import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tag_details.domain.TagDetailsRepository
import com.example.tagplayer.tag_details.domain.errors.TagDetailsException

class TagDetailsRepositoryImpl(
    private val cacheDatasource: TagDetailsDatasource,
    private val handleTry: HandleTry<TagDetailsException>,
) : TagDetailsRepository {

    override suspend fun add(id: Long, title: String, color: String) = handleTry
        .handleAsync(TagDetailsException.Add()) { cacheDatasource.add(id, title, color) }

    override suspend fun tag(id: Long): SongTag = handleTry
        .handleAsync(TagDetailsException.Details()) { cacheDatasource.tag(id) }
}