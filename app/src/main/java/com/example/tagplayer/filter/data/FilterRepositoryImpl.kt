package com.example.tagplayer.filter.data

import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.filter.domain.FilterDomain
import com.example.tagplayer.filter.domain.errors.FilterException
import com.example.tagplayer.filter.domain.FilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilterRepositoryImpl(
    private val handleTry: HandleTry<FilterException>,
    private val cacheDatasource: FilterCacheDatasource,
    private val mapper: SongTag.Mapper<FilterDomain>,
) : FilterRepository<FilterDomain> {

    override fun tags(): Flow<List<FilterDomain>> = handleTry.handle(FilterException.Fetch()) {
        cacheDatasource.tags().map { list -> list.map { it.map(mapper) } }
    }

    override suspend fun save(filter: Pair<Long, Boolean>) = handleTry
        .handleAsync(FilterException.Save()) {
            cacheDatasource.save(filter)
        }

    override suspend fun reset() = handleTry.handleAsync(FilterException.Clear()) {
        cacheDatasource.reset()
    }
}