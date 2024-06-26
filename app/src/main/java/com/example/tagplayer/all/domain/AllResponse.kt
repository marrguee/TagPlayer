package com.example.tagplayer.all.domain

import com.example.tagplayer.all.presentation.AllState
import com.example.tagplayer.all.presentation.SongUi
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface AllResponse {
    fun map(mapper: AllMapper, coroutineScope: CoroutineScope)
    interface AllMapper {
        fun mapSuccess(flow: Flow<List<SongUi>>, coroutineScope: CoroutineScope)
        fun mapError(msg: String, coroutineScope: CoroutineScope)

        class Base(
            private val communication: Communication<AllState>,
            private val dispatcherList: DispatcherList
        ) : AllMapper {
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
        override fun map(mapper: AllMapper, coroutineScope: CoroutineScope) {
            mapper.mapSuccess(flow, coroutineScope)
        }
    }

    class TracksAllResponseError(
        private val msg: String
    ) : AllResponse {
        override fun map(mapper: AllMapper, coroutineScope: CoroutineScope) {
            mapper.mapError(msg, coroutineScope)
        }
    }
}