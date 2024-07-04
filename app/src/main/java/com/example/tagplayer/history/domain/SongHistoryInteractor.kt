package com.example.tagplayer.history.domain

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.domain.UpdateSongHistory
import kotlinx.coroutines.flow.map

interface SongHistoryInteractor : PlaySongForeground {
    fun playedHistory() : HistoryResponse

    class Base(
        private val repository: SongHistoryRepository<HistoryDomain>,
        private val handleError: HandleError<DomainError, String>,
    ) : SongHistoryInteractor {
        override fun playedHistory(): HistoryResponse {
            return try {
                HistoryResponse.HistoryResponseSuccess(
                    repository.handleRequest().map {
                        list -> list.map { it.map() }
                    }
                )
            } catch (e: DomainError) {
                HistoryResponse.HistoryResponseError(handleError.handle(e))
            }
        }

        override fun playSongForeground(id: Long) {
            repository.playSongForeground(id)
        }
    }
}