package com.example.tagplayer.tag_details.domain

import com.example.tagplayer.core.domain.HandleResponse

interface TagDetailsInteractor {
    suspend fun tag(id: Long): TagDetailsResponse
    suspend fun add(title: String, color: String, id: Long = 0) : TagDetailsResponse

    class Base(
        private val repository: TagDetailsRepository,
        private val handleResponse: HandleResponse.All<TagDetailsResponse>,
    ) : TagDetailsInteractor {

        override suspend fun tag(id: Long) : TagDetailsResponse = handleResponse.handleAsync {
            repository.tag(id).let { TagDetailsResponse.Success(it.title, it.color) }
        }

        override suspend fun add(title: String, color: String, id: Long) : TagDetailsResponse =
            handleResponse.handleAsyncEmpty { repository.add(id, title, color) }
    }
}
