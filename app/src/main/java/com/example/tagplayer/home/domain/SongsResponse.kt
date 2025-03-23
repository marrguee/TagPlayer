package com.example.tagplayer.home.domain

import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.home.presentation.HomeState
import com.example.tagplayer.home.presentation.SongUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

interface SongsResponse {
    fun map(mapper: Mapper, scope: CoroutineScope)

    interface Mapper {
        fun mapFlow(flow: Flow<List<SongUi>>, scope: CoroutineScope)
        fun mapList(list: List<SongUi>)
        fun mapError(error: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<HomeState>,
            private val dispatcherList: DispatcherList,
        ) : Mapper {
            private var job: Job? = null

            override fun mapFlow(flow: Flow<List<SongUi>>, scope: CoroutineScope) {
                job?.cancel()
                job = scope.launch(dispatcherList.ui()) {
                    flow.cancellable().collect {
                        observable.update(HomeState.LibraryUpdated(it))
                    }
                }
            }

            override fun mapList(list: List<SongUi>) =
                observable.update(HomeState.RecentlyUpdated(list))

            override fun mapError(error: String) =
                observable.update(HomeState.Error(error))
        }
    }

    data class Library(private val flow: Flow<List<SongUi>>): SongsResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) {
            mapper.mapFlow(flow, scope)
        }
    }

    data class Recently(private val list: List<SongUi>): SongsResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) {
            mapper.mapList(list)
        }
    }

    data class Error(private val error: String) : SongsResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) {
            mapper.mapError(error)
        }
    }
}