package com.example.tagplayer.all.domain

import com.example.tagplayer.all.presentation.TrackUi

interface Interactor {
    suspend fun tracks() : Response

    class Base(
        private val repository: AllRepository<TrackDomain>,
        private val handleError: HandleError<DomainError, String>,
        private val modelMapper: ModelMapper<TrackUi>
    ) : Interactor {
        override suspend fun tracks() = try {
            Response.TracksResponseSuccess(repository.tracks().map { it.map(modelMapper) })
        } catch (e: DomainError) {
            Response.TracksResponseError(handleError.handle(e))
        }


    }
}