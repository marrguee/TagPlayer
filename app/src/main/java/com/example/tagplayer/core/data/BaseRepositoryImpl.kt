package com.example.tagplayer.core.data

import com.example.tagplayer.all.domain.AllRepository
import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.history.domain.SongHistoryRepository
import com.example.tagplayer.history.domain.HistoryDomain
import com.example.tagplayer.search.data.SearchHistory
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.domain.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseRepositoryImpl(
    private val cacheDatasource: CacheDatasource,
    private val foregroundWrapper: ForegroundWrapper,
    private val handleError: HandleError<Exception, DomainError>,
    private val songModelMapper: SongData.Mapper<SongDomain>,
    private val historyModelMapper: SongLastPlayedCrossRef.Mapper<HistoryDomain>,
    private val searchModelMapper: SearchHistory.Mapper<SearchDomain>
) : AllRepository<SongDomain>, SongHistoryRepository<HistoryDomain>, SearchRepository<SearchDomain, SearchHistory> {
    override fun songsFlow(): Flow<List<SongDomain>> = try {
        cacheDatasource.songs().map {
            list -> list.map { it.map(songModelMapper) }
        }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }

    override fun searchSongsForeground() {
        foregroundWrapper.scanMedia()
    }

    override fun playedHistory(): Flow<List<HistoryDomain>> = try {
        cacheDatasource.history().map { list ->
            historyModelMapper.map(list)
        }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }

    override fun playSongForeground(id: Long) {
        foregroundWrapper.playMedia(id)
    }

    override suspend fun songToHistory(lastPlayed: LastPlayed) {
        cacheDatasource.songToHistory(lastPlayed)
    }

    override suspend fun findSongs(query: String) : List<SongDomain> = try {
        cacheDatasource.findSongs(query).map {
            it.map(songModelMapper)
        }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }


    override suspend fun searchHistory(): List<SearchDomain> =
        cacheDatasource.searchHistory().map { it.map(searchModelMapper) }

    override suspend fun updateSearch(searchHistory: SearchHistory) =
        cacheDatasource.updateSearch(searchHistory)
}