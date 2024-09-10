package com.example.tagplayer.search.data

import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.domain.SearchRepository

class SearchRepositoryImpl(
    handleError: HandleError<Exception, DomainError>,
    foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: SearchCacheDatasource.Base,
    private val songModelMapper: Song.Mapper<SongDomain>,
    private val searchModelMapper: SearchHistoryTable.Mapper<SearchDomain>,
) :
    AbstractSongBasedRepository<Song, SongDomain, String>(foregroundWrapper, handleError),
    SearchRepository<SearchDomain, SongDomain> {

    override suspend fun findSongsByTitle(songTitle: String): List<SongDomain>  =
        cacheDatasource.findSongsByTitle(songTitle).map { it.map(songModelMapper) }

    override suspend fun searchHistory(): List<SearchDomain> =
        cacheDatasource.searchHistory().map { it.map(searchModelMapper) }

    override suspend fun updateSearch(searchHistoryTable: SearchHistoryTable) =
        cacheDatasource.updateSearch(searchHistoryTable)

}