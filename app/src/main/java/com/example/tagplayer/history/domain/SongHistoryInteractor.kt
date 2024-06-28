package com.example.tagplayer.history.domain

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.data.LastPlayed
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.domain.UpdateSongHistory
import kotlinx.coroutines.flow.map

interface SongHistoryInteractor : PlaySongForeground, UpdateSongHistory {
    fun playedHistory() : HistoryResponse

    class Base(
        private val repository: SongHistoryRepository<HistoryDomain>,
        private val handleError: HandleError<DomainError, String>,
    ) : SongHistoryInteractor {
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

        override suspend fun songToHistory(lastPlayed: LastPlayed) {
            repository.songToHistory(lastPlayed)
        }
    }
}