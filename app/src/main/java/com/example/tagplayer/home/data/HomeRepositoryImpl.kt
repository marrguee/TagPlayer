package com.example.tagplayer.home.data

import com.example.tagplayer.home.domain.HomeRepository
import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.HomeCacheDatasource
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    handleError: HandleError<Exception, DomainError>,
    private val foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: HomeCacheDatasource.Base,
    private val songModelMapper: Song.Mapper<SongDomain>,
) : AbstractSongBasedRepository<Song, SongDomain, Any>(foregroundWrapper, handleError),
    HomeRepository<SongDomain>
{
    override fun library(): Flow<List<SongDomain>> =
        cacheDatasource.library().map { list -> list.map { it.map(songModelMapper) } }

    override suspend fun recently(): List<SongDomain> =
        cacheDatasource.recently().map { it.map(songModelMapper) }

    override fun scan() {
        foregroundWrapper.scanMedia()
    }

}