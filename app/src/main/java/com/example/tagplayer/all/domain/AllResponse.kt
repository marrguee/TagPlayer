package com.example.tagplayer.all.domain

import com.example.tagplayer.all.presentation.AllState
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface AllResponse {
    fun map(mapper: AllResponseMapper, coroutineScope: CoroutineScope)
    interface AllResponseMapper {
        fun mapSuccess(flow: Flow<List<SongUi>>, coroutineScope: CoroutineScope)
        fun mapError(msg: String, coroutineScope: CoroutineScope)

        class Base(
            private val communication: Communication<AllState>,
            private val dispatcherList: DispatcherList
        ) : AllResponseMapper {
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

    class TracksAllResponseSuccess(
        private val flow: Flow<List<SongUi>>
    ) : AllResponse {
        override fun map(mapper: AllResponseMapper, coroutineScope: CoroutineScope) {
            mapper.mapSuccess(flow, coroutineScope)
        }
    }

    class TracksAllResponseError(
        private val msg: String
    ) : AllResponse {
        override fun map(mapper: AllResponseMapper, coroutineScope: CoroutineScope) {
            mapper.mapError(msg, coroutineScope)
        }
    }
}