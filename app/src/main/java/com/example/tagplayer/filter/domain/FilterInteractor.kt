package com.example.tagplayer.filter.domain

import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.filter.presentation.FilterUi
import kotlinx.coroutines.flow.map

interface FilterInteractor {
    suspend fun tags(): FilterResponse
    suspend fun save(filter: Pair<Long, Boolean>) : FilterResponse
    suspend fun reset() : FilterResponse

    class Base(
        private val repository: FilterRepository<FilterDomain>,
        private val mapper: FilterDomain.Mapper<FilterUi>,
        private val handleResponse: HandleResponse.All<FilterResponse>,
    ) : FilterInteractor {

        override suspend fun tags(): FilterResponse = handleResponse.handleAsync {
            FilterResponse.Success(repository.tags().map { list -> list.map { it.map(mapper) } })
        }

        override suspend fun save(filter: Pair<Long, Boolean>) = handleResponse.handleAsyncEmpty {
            repository.save(filter)
        }

        override suspend fun reset() = handleResponse.handleAsyncEmpty {
            repository.reset()
        }
    }
}

