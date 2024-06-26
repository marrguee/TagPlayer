package com.example.tagplayer.all.domain

import com.example.tagplayer.core.data.LastPlayed
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.all.presentation.SongUi
import com.example.tagplayer.core.domain.UpdateHistory
import kotlinx.coroutines.flow.map

interface AllInteractor : PlaySongForeground, UpdateHistory {
    fun tracksFlow() : AllResponse
    fun searchSongsForeground()

    class Base(
        private val repository: AllRepository<SongDomain>,
        private val handleError: HandleError<DomainError, String>,
        private val modelMapper: SongDomain.Mapper<SongUi>
    ) : AllInteractor {
        override fun tracksFlow() = try {
            AllResponse.TracksAllResponseSuccess(
                repository.songsFlow().map {
                    list -> list.map { it.map(modelMapper) }
                }
            )
        } catch (e: DomainError) {
            AllResponse.TracksAllResponseError(handleError.handle(e))
        }

        override fun searchSongsForeground() {
            repository.searchSongsForeground()
        }

        override fun playSongForeground(id: Long) {
            repository.playSongForeground(id)
        }

        override suspend fun updateHistory(lastPlayed: LastPlayed) {
            repository.updateHistory(lastPlayed)
        }
    }
}