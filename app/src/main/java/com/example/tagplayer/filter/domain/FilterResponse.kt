package com.example.tagplayer.filter.domain

import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.filter.presentation.FilterState
import com.example.tagplayer.filter.presentation.FilterUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

interface FilterResponse {
    fun map(mapper: Mapper, scope: CoroutineScope)

    interface Mapper {
        fun mapSuccess(flow: Flow<List<FilterUi>>, scope: CoroutineScope)
        fun mapError(error: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<FilterState>,
            private val dispatcherList: DispatcherList,
        ) : Mapper {
            private var job: Job? = null

            override fun mapSuccess(flow: Flow<List<FilterUi>>, scope: CoroutineScope) {
                job?.cancel()
                job = scope.launch(dispatcherList.ui()) {
                    flow.cancellable().collect {
                        observable.update(FilterState.Filters(it))
                    }
                }
            }

            override fun mapError(error: String) {
                observable.update(FilterState.Error(error))
            }
        }
    }

    class Success(private val tags: Flow<List<FilterUi>>) : FilterResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) = mapper.mapSuccess(tags, scope)
    }

    object Empty : FilterResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) = Unit
    }

    class Error(private val error: String) : FilterResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) = mapper.mapError(error)
    }
}