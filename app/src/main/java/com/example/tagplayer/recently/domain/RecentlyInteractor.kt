package com.example.tagplayer.recently.domain

import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.domain.PlayForeground

interface RecentlyInteractor : PlayForeground {
    suspend fun recently() : RecentlyResponse

    class Base(
        private val repository: RecentlyRepository<RecentlyDomain>,
        private val handleResponse: HandleResponse.Handle<RecentlyResponse>,
    ) : RecentlyInteractor {

        override suspend fun recently() : RecentlyResponse = handleResponse.handleAsync {
            RecentlyResponse.RecentlyResponseSuccess(repository.recently().map { it.map() })
        }

        override fun play(id: Long) = repository.play(id)
    }
}