package com.example.tagplayer.recently.data

import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.recently.domain.RecentlyDomain
import com.example.tagplayer.recently.domain.RecentlyRepository

class RecentlyRepositoryImpl(
    private val handleError: HandleError<Exception, DomainError>,
    foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: RecentlyCacheDatasource,
    private val historyModelMapper: SongLastPlayedCrossRef.Mapper<RecentlyDomain>,
) :
    AbstractSongBasedRepository(foregroundWrapper),
    RecentlyRepository<RecentlyDomain>
{
    override suspend fun recently(): List<RecentlyDomain> = try {
        historyModelMapper.map(cacheDatasource.recently())
    } catch (e: Exception) {
        throw handleError.handle(e)
    }



}