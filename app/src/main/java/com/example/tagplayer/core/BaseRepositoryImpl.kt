package com.example.tagplayer.core

import com.example.tagplayer.all.domain.AllRepository
import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.ModelMapper
import com.example.tagplayer.all.domain.TrackDomain

class BaseRepositoryImpl(
    private val cacheDatasource: CacheDatasource,
    private val handleError: HandleError<Exception, DomainError>,
    private val modelMapper: ModelMapper<TrackDomain>
) : AllRepository<TrackDomain> {
    override suspend fun tracks(): List<TrackDomain> = try {
        cacheDatasource.tracks().map { it.map(modelMapper) }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }
}