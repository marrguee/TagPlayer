package com.example.tagplayer.history.domain

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.data.LastPlayed
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.domain.UpdateHistory
import kotlinx.coroutines.flow.map

interface HistoryInteractor : PlaySongForeground, UpdateHistory {
    fun playedHistory() : HistoryResponse

    class Base(
        private val repository: HistoryRepository<HistoryDomain>,
        private val handleError: HandleError<DomainError, String>,
    ) : HistoryInteractor {
        override fun playedHistory(): HistoryResponse {
            return try {
                val d = repository.playedHistory().map {
                        list -> list.map { it.map() }
                }
                HistoryResponse.HistoryResponseSuccess(
                    d
                )
            } catch (e: DomainError) {
                HistoryResponse.HistoryResponseError(handleError.handle(e))
            }
        }

        override fun playSongForeground(id: Long) {
            repository.playSongForeground(id)
        }

        override suspend fun updateHistory(lastPlayed: LastPlayed) {
            repository.updateHistory(lastPlayed)
        }
    }
}