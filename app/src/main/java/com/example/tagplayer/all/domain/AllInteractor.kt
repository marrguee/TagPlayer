package com.example.tagplayer.all.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.main.presentation.SongUi
import kotlinx.coroutines.flow.map

interface AllInteractor : PlaySongForeground, ScanSongsForeground {
    fun tracksFlow() : AllResponse

    class Base(
        private val repository: AllRepository<SongDomain>,
        private val handleError: HandleError<DomainError, String>,
        private val modelMapper: SongDomain.Mapper<SongUi>
    ) : AllInteractor {
        override fun tracksFlow() = try {
            AllResponse.TracksAllResponseSuccess(
                repository.handleRequest().map {
                    list -> list.map { it.map(modelMapper) }
                }
            )
        } catch (e: DomainError) {
            AllResponse.TracksAllResponseError(handleError.handle(e))
        }

        override fun playSongForeground(id: Long) =
            repository.playSongForeground(id)

        override fun scan() =
            repository.scan()
    }
}