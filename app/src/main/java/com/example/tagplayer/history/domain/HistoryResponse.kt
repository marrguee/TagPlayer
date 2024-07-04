package com.example.tagplayer.history.domain

import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.history.presentation.HistoryState
import com.example.tagplayer.main.presentation.ItemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface HistoryResponse {
    fun map(mapper: HistoryResponseMapper, coroutineScope: CoroutineScope)
    interface HistoryResponseMapper {
        fun mapSuccess(flow: Flow<List<ItemUi>>, coroutineScope: CoroutineScope)
        fun mapError(msg: String, coroutineScope: CoroutineScope)

        class Base(
            private val communication: Communication<HistoryState>,
            private val dispatcherList: DispatcherList
        ) : HistoryResponseMapper {
            override fun mapSuccess(flow: Flow<List<ItemUi>>, coroutineScope: CoroutineScope) {
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
        private val flow: Flow<List<ItemUi>>
    ) : HistoryResponse {
        override fun map(mapper: HistoryResponseMapper, coroutineScope: CoroutineScope) {
            mapper.mapSuccess(flow, coroutineScope)
        }
    }

    class HistoryResponseError(
        private val msg: String
    ) : HistoryResponse {
        override fun map(mapper: HistoryResponseMapper, coroutineScope: CoroutineScope) {
            mapper.mapError(msg, coroutineScope)
        }
    }
}