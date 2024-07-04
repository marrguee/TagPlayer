package com.example.tagplayer.all.data

import com.example.tagplayer.all.domain.AllRepository
import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.AllCacheDatasource
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.Song
import kotlinx.coroutines.flow.Flow

class AllRepositoryImpl(
    handleError: HandleError<Exception, DomainError>,
    private val foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: AllCacheDatasource.Base,
    private val songModelMapper: Song.Mapper<SongDomain>,
) : AbstractSongBasedRepository<Song, SongDomain, Any>(foregroundWrapper, handleError),
    AllRepository<SongDomain>
{

    override fun scan() {
        foregroundWrapper.scanMedia()
    }

    override fun cacheDatasourceData(vararg params: Any): Flow<List<Song>> =
        cacheDatasource.handleRequest()

    override fun mapList(list: List<Song>): List<SongDomain> =
        list.map { it.map(songModelMapper) }

}