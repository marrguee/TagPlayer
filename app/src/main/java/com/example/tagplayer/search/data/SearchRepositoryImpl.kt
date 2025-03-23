package com.example.tagplayer.search.data

import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.search.domain.SearchRepository
import com.example.tagplayer.search.domain.SearchDomain

class SearchRepositoryImpl(
    foregroundWrapper: ForegroundWrapper,
    private val handleTry: HandleTry<Exception>,
    private val cacheDatasource: SearchCacheDatasource,
    private val mapper: Song.Mapper<SearchDomain>
) : AbstractSongBasedRepository(foregroundWrapper), SearchRepository<SearchDomain> {

    override suspend fun search(query: String): List<SearchDomain> = handleTry
        .handleAsync(Exception()) {
            cacheDatasource.search(query).map { it.map(mapper) }
        }
}