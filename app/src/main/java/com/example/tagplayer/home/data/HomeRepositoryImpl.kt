package com.example.tagplayer.home.data

import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.home.domain.HomeRepository
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.home.domain.SortingType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    foregroundWrapper: ForegroundWrapper,
    private val handleError: HandleError<Exception, DomainError>,
    private val cacheDatasource: HomeCacheDatasource,
    private val songModelMapper: Song.Mapper<SongDomain>,
    private val recentlyModelMapper: SongLastPlayedCrossRef.Mapper<SongDomain>,
) : AbstractSongBasedRepository(foregroundWrapper),
    HomeRepository<SongDomain>
{
    override fun library(sortingType: SortingType): Flow<List<SongDomain>> = try {
        cacheDatasource.library(sortingType.map()).map { list -> list.map { it.map(songModelMapper) } }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }

    override suspend fun filters(): List<Long> = try {
        cacheDatasource.filters()
    } catch (e: Exception) {
        throw handleError.handle(e)
    }

    override fun filtered(tags: List<Long>, sortingType: SortingType): Flow<List<SongDomain>> = try {
        cacheDatasource.filtered(sortingType.map(tags)).map {
                flow -> flow.map { list -> list.map(songModelMapper)}
        }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }

    override suspend fun croppedRecently(): List<SongDomain> {
        return recentlyModelMapper.map(cacheDatasource.croppedRecently())
    }

    override fun scan() {
        cacheDatasource.scan()
    }

}