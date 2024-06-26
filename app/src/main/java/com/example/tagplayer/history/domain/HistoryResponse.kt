package com.example.tagplayer.history.domain

import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.history.presentation.HistoryState
import com.example.tagplayer.history.presentation.HistoryUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface HistoryResponse {
    fun map(mapper: HistoryMapper, coroutineScope: CoroutineScope)
    interface HistoryMapper {
        fun mapSuccess(flow: Flow<List<HistoryUi>>, coroutineScope: CoroutineScope)
        fun mapError(msg: String, coroutineScope: CoroutineScope)

        class Base(
            private val communication: Communication<HistoryState>,
            private val dispatcherList: DispatcherList
        ) : HistoryMapper {
            override fun mapSuccess(flow: Flow<List<HistoryUi>>, coroutineScope: CoroutineScope) {
                coroutineScope.launch {
                    flow.flowOn(dispatcherList.io()).collect { songs ->
                        communication.update(HistoryState.HistoryUpdated(songs))
                    }
                }
            }

            override fun mapError(msg: String, coroutineScope: CoroutineScope) {
                communication.update(HistoryState.Error(msg))
            }

        }
    }

    class HistoryResponseSuccess(
        private val flow: Flow<List<HistoryUi>>
    ) : HistoryResponse {
        override fun map(mapper: HistoryMapper, coroutineScope: CoroutineScope) {
            mapper.mapSuccess(flow, coroutineScope)
        }
    }

    class HistoryResponseError(
        private val msg: String
    ) : HistoryResponse {
        override fun map(mapper: HistoryMapper, coroutineScope: CoroutineScope) {
            mapper.mapError(msg, coroutineScope)
        }
    }
}