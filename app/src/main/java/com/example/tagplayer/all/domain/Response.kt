package com.example.tagplayer.all.domain

import com.example.tagplayer.all.presentation.AllState
import com.example.tagplayer.all.presentation.SongUi
import com.example.tagplayer.core.Communication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Response {
    fun map(mapper: Mapper, coroutineScope: CoroutineScope)
    interface Mapper {
        fun mapSuccess(flow: Flow<List<SongUi>>, coroutineScope: CoroutineScope)
        fun mapError(msg: String, coroutineScope: CoroutineScope)

        class AllMapper(
            private val communication: Communication<AllState>,
            private val dispatcherList: DispatcherList
        ) : Mapper {
            override fun mapSuccess(flow: Flow<List<SongUi>>, coroutineScope: CoroutineScope) {
                coroutineScope.launch {
                    flow.flowOn(dispatcherList.io()).collect { songs ->
                        communication.update(AllState.TracksUpdated(songs))
                    }
                }
            }

            override fun mapError(msg: String, coroutineScope: CoroutineScope) {
                communication.update(AllState.Error(msg))
            }

        }
    }

    class TracksResponseSuccess(
        private val flow: Flow<List<SongUi>>
    ) : Response {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) {
            mapper.mapSuccess(flow, coroutineScope)
        }
    }

    class TracksResponseError(
        private val msg: String
    ) : Response {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) {
            mapper.mapError(msg, coroutineScope)
        }
    }
}