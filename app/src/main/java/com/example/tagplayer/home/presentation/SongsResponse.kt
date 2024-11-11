package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.main.presentation.SongUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface SongsResponse {
    fun map(mapper: SongsResponseMapper)

    interface SongsResponseMapper {
        fun mapFlow(flow: Flow<List<SongUi>>)
        fun mapError(error: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<HomeState>,
        ) : SongsResponseMapper {
            private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
            private var flowJob: Job? = null

            override fun mapFlow(flow: Flow<List<SongUi>>) {
                flowJob?.cancel()
                flowJob = scope.launch {
                    flow.collect {
                        observable.update(HomeState.LibraryUpdated(it))
                    }
                }
            }

            override fun mapError(error: String) {
                observable.update(HomeState.Error(error))
            }
        }
    }

    class SelectedLibrary(private val flow: Flow<List<SongUi>>): SongsResponse {
        override fun map(mapper: SongsResponseMapper) {
            mapper.mapFlow(flow)
        }
    }

    class Error(private val error: String) : SongsResponse {
        override fun map(mapper: SongsResponseMapper) {
            mapper.mapError(error)
        }
    }
}