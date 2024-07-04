package com.example.tagplayer.search.data

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.domain.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    handleError: HandleError<Exception, DomainError>,
    foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: SearchCacheDatasource.Base,
    private val songModelMapper: Song.Mapper<SongDomain>,
    private val searchModelMapper: SearchHistoryTable.Mapper<SearchDomain>,
) : AbstractSongBasedRepository<Song, SongDomain, String>(foregroundWrapper, handleError),
    SearchRepository<SearchDomain, SongDomain>
{
    override fun cacheDatasourceData(vararg params: String): Flow<List<Song>> =
        cacheDatasource.handleRequest(*params)

    override fun mapList(list: List<Song>): List<SongDomain> =
        list.map { it.map(songModelMapper) }

    override suspend fun searchHistory(): List<SearchDomain> =
        cacheDatasource.searchHistory().map { it.map(searchModelMapper) }

    override suspend fun updateSearch(searchHistory: SearchHistoryTable) =
        cacheDatasource.updateSearch(searchHistory)

}