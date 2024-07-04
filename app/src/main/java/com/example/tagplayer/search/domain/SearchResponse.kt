package com.example.tagplayer.search.domain

import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.search.presentation.SearchUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface SearchResponse {
    fun map(mapper: SearchResponseMapper, coroutineScope: CoroutineScope)

    interface SearchResponseMapper {
        fun mapHistorySuccess(list: List<SearchUi>, coroutineScope: CoroutineScope)

        fun mapSongsSuccess(flow: Flow<List<SongUi>>, coroutineScope: CoroutineScope)
        fun mapError(cause: String)

        class Base(
            private val communication: Communication<SearchState>,
            private val dispatcherList: DispatcherList
        ) : SearchResponseMapper {
            override fun mapHistorySuccess(list: List<SearchUi>, coroutineScope: CoroutineScope) {
                communication.update(SearchState.HistorySuccess(list))
            }

            override fun mapSongsSuccess(flow: Flow<List<SongUi>>, coroutineScope: CoroutineScope) {
                coroutineScope.launch {
                    flow.flowOn(dispatcherList.io()).collect {
                        communication.update(SearchState.SongsSuccess(it))
                    }
                }
            }

            override fun mapError(cause: String) {
                communication.update(SearchState.Error(cause))
            }
        }
    }

    class SearchSuccess(private val list: List<SearchUi>) : SearchResponse {
        override fun map(mapper: SearchResponseMapper, coroutineScope: CoroutineScope){
            mapper.mapHistorySuccess(list, coroutineScope)
        }
    }

    class SongsSuccess(private val flow: Flow<List<SongUi>>) : SearchResponse {
        override fun map(mapper: SearchResponseMapper, coroutineScope: CoroutineScope){
            mapper.mapSongsSuccess(flow, coroutineScope)
        }
    }

    class Error(private val cause: String) : SearchResponse {
        override fun map(mapper: SearchResponseMapper, coroutineScope: CoroutineScope) {
            mapper.mapError(cause)
        }
    }
}