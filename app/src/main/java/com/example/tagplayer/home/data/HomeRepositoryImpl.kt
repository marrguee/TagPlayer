package com.example.tagplayer.home.data

import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.home.domain.HomeRepository
import com.example.tagplayer.home.domain.OrderType
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.home.domain.errors.HomeException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    private val foregroundWrapper: ForegroundWrapper,
    private val handleTry: HandleTry<HomeException>,
    private val cacheDatasource: HomeCacheDatasource,
    private val mapper: Song.Mapper<SongDomain>,
    private val recentlyModelMapper: SongLastPlayedCrossRef.Mapper<SongDomain>,
) : AbstractSongBasedRepository(foregroundWrapper), HomeRepository<SongDomain> {

    override suspend fun croppedRecently(): List<SongDomain> = handleTry
        .handleAsync(HomeException.Recently()) {
            recentlyModelMapper.map(cacheDatasource.croppedRecently())
        }

    override fun sorted(field: ObtainFieldName, order: OrderType): Flow<List<SongDomain>> =
        handleTry.handle(HomeException.Library()) {
            cacheDatasource.sorted(field, order).map { list -> list.map { it.map(mapper)} }
        }

    override fun scan() = foregroundWrapper.scanMedia()
}