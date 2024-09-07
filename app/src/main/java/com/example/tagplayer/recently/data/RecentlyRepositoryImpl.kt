package com.example.tagplayer.recently.data

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.recently.domain.RecentlyDomain
import com.example.tagplayer.recently.domain.RecentlyRepository

class RecentlyRepositoryImpl(
    handleError: HandleError<Exception, DomainError>,
    foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: RecentlyCacheDatasource,
    private val historyModelMapper: SongLastPlayedCrossRef.Mapper<RecentlyDomain>,
) :
    AbstractSongBasedRepository<SongLastPlayedCrossRef, RecentlyDomain, Any>(foregroundWrapper, handleError),
    RecentlyRepository<RecentlyDomain>
{
    override suspend fun recently(): List<RecentlyDomain> =
        historyModelMapper.map(cacheDatasource.recently())


}