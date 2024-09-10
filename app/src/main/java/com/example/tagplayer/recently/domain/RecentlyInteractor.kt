package com.example.tagplayer.recently.domain

import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.core.domain.PlaySongForeground

interface RecentlyInteractor : PlaySongForeground {
    suspend fun recently() : RecentlyResponse

    class Base(
        private val repository: RecentlyRepository<RecentlyDomain>,
        private val handleError: HandleError<DomainError, String>,
    ) : RecentlyInteractor {

        override suspend fun recently() : RecentlyResponse {
            return try {
                RecentlyResponse.RecentlyResponseSuccess(
                    repository.recently().map { it.map() }
                )
            } catch (e: DomainError) {
                RecentlyResponse.RecentlyResponseError(handleError.handle(e))
            }
        }

        override fun playSongForeground(id: Long) {
            repository.playSongForeground(id)
        }
    }
}