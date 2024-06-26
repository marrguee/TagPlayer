package com.example.tagplayer.core.data

import com.example.tagplayer.all.domain.AllRepository
import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.history.domain.HistoryRepository
import com.example.tagplayer.history.domain.HistoryDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseRepositoryImpl(
    private val cacheDatasource: CacheDatasource,
    private val foregroundWrapper: ForegroundWrapper,
    private val handleError: HandleError<Exception, DomainError>,
    private val songModelMapper: SongData.Mapper<SongDomain>,
    private val historyModelMapper: SongLastPlayedCrossRef.Mapper<HistoryDomain>
) : AllRepository<SongDomain>, HistoryRepository<HistoryDomain> {
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

    override suspend fun updateHistory(lastPlayed: LastPlayed) {
        cacheDatasource.updateHistory(lastPlayed)
    }
}