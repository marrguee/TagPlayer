package com.example.tagplayer.core.data

import com.example.tagplayer.all.domain.AllRepository
import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseRepositoryImpl(
    private val cacheDatasource: CacheDatasource,
    private val foregroundWrapper: ForegroundWrapper,
    private val handleError: HandleError<Exception, DomainError>,
    private val modelMapper: SongData.Mapper<SongDomain>
) : AllRepository<SongDomain> {
    override fun songsFlow(): Flow<List<SongDomain>> = try {
        cacheDatasource.songs().map {
            list -> list.map { it.map(modelMapper) }
        }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }

    override fun searchSongsForeground() {
        foregroundWrapper.scanMedia()
    }

    override fun playSongForeground(id: Long) {
        foregroundWrapper.playMedia(id)
    }
}