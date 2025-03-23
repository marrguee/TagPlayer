package com.example.tagplayer.search.domain

import com.example.tagplayer.core.domain.PlayForeground
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.search.presentation.SearchUi

interface SearchInteractor : PlayForeground {
    suspend fun search(query: String) : SearchResponse

    class Base(
        private val repository: SearchRepository<SearchDomain>,
        private val handleResponse: HandleResponse.Handle<SearchResponse>,
        private val mapper: SearchDomain.Mapper<SearchUi>
    ) : SearchInteractor {

        override suspend fun search(query: String) = handleResponse.handleAsync {
            SearchResponse.SongsSuccess(repository.search(query).map { it.map(mapper) })
        }

        override fun play(id: Long) = repository.play(id)
    }
}