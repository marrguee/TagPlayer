package com.example.tagplayer.all.domain

import com.example.tagplayer.all.presentation.SongUi
import kotlinx.coroutines.flow.map

interface Interactor {
    fun tracksFlow() : Response
    fun searchSongsForeground()

    class Base(
        private val repository: AllRepository<SongDomain>,
        private val handleError: HandleError<DomainError, String>,
        private val modelMapper: SongDomain.Mapper<SongUi>
    ) : Interactor {
        override fun tracksFlow() = try {
            Response.TracksResponseSuccess(
                repository.songsFlow().map {
                        list -> list.map { it.map(modelMapper) }
                }
            )
        } catch (e: DomainError) {
            Response.TracksResponseError(handleError.handle(e))
        }

        override fun searchSongsForeground() {
            repository.searchSongsForeground()
        }


    }
}