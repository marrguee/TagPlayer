package com.example.tagplayer.history.data

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.history.domain.HistoryDomain
import com.example.tagplayer.history.domain.SongHistoryRepository
import kotlinx.coroutines.flow.Flow

class HistoryRepositoryImpl(
    handleError: HandleError<Exception, DomainError>,
    foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: HistoryCacheDatasource.Base,
    private val historyModelMapper: SongLastPlayedCrossRef.Mapper<HistoryDomain>,
) : AbstractSongBasedRepository<SongLastPlayedCrossRef, HistoryDomain, Any>(foregroundWrapper, handleError),
    SongHistoryRepository<HistoryDomain>
{
    override fun cacheDatasourceData(vararg params: Any): Flow<List<SongLastPlayedCrossRef>> =
        cacheDatasource.handleRequest()

    override fun mapList(list: List<SongLastPlayedCrossRef>): List<HistoryDomain> =
        historyModelMapper.map(list)

}