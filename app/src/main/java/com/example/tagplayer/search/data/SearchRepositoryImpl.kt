package com.example.tagplayer.search.data

import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.search.domain.SearchRepository
import com.example.tagplayer.search.domain.SongSearchDomain

class SearchRepositoryImpl(
    private val handleError: HandleError<Exception, DomainError>,
    foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: SearchCacheDatasource.Base,
    private val songModelMapper: Song.Mapper<SongSearchDomain>
) : AbstractSongBasedRepository(foregroundWrapper),
    SearchRepository<SongSearchDomain> {

    override suspend fun findSongsByTitle(songTitle: String): List<SongSearchDomain> =
        try {
            cacheDatasource.findSongsByTitle(songTitle).map { it.map(songModelMapper) }
        } catch (e: Exception) {
            throw handleError.handle(e)
        }
}