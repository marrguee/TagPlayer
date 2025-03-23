package com.example.tagplayer.recently.data

import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.recently.domain.RecentlyDomain
import com.example.tagplayer.recently.domain.RecentlyRepository

class RecentlyRepositoryImpl(
    foregroundWrapper: ForegroundWrapper,
    private val handleTry: HandleTry<Exception>,
    private val cacheDatasource: RecentlyCacheDatasource,
    private val mapper: SongLastPlayedCrossRef.Mapper<RecentlyDomain>,
) : AbstractSongBasedRepository(foregroundWrapper), RecentlyRepository<RecentlyDomain> {

    override suspend fun recently(): List<RecentlyDomain> = handleTry.handleAsync(Exception()) {
        mapper.map(cacheDatasource.recently())
    }
}