package com.example.tagplayer.tags_attach.domain

import com.example.tagplayer.tags_attach.presentation.TagUi
import com.example.tagplayer.core.domain.HandleResponse
import kotlinx.coroutines.flow.map

interface AttachTagsInteractor {
    fun tags(songId: Long) : TagsResponse
    suspend fun add(songId: Long, tagId: Long) : TagsResponse
    suspend fun remove(songId: Long, tagId: Long) : TagsResponse

    class Base(
        private val repository: AttachTagsRepository<TagDomain>,
        private val mapper: TagDomain.Mapper<TagUi>,
        private val handleResponse: HandleResponse.All<TagsResponse>,
    ) : AttachTagsInteractor {

        override fun tags(songId: Long): TagsResponse = handleResponse.handle {
            val all = repository.all(songId).map { list -> list.map { it.map(mapper) } }
            val owned = repository.owned(songId).map { list -> list.map { it.map(mapper) } }
            TagsResponse.Success(all, owned)
        }

        override suspend fun add(songId: Long, tagId: Long) : TagsResponse = handleResponse
            .handleAsyncEmpty {
                repository.add(songId, tagId)
            }

        override suspend fun remove(songId: Long, tagId: Long) : TagsResponse = handleResponse
            .handleAsyncEmpty {
                repository.remove(songId, tagId)
            }
    }
}