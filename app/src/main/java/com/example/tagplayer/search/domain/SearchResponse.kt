package com.example.tagplayer.search.domain

import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.search.presentation.SearchUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface SearchResponse {
    fun map(mapper: SearchResponseMapper)

    interface SearchResponseMapper {
        fun mapSearchHistorySuccess(list: List<SearchUi>)

        fun mapSongsSuccess(list: List<SongUi>)
        fun mapError(cause: String)

        class Base(
            private val communication: Communication<SearchState>,
            private val dispatcherList: DispatcherList
        ) : SearchResponseMapper {
            override fun mapSearchHistorySuccess(list: List<SearchUi>) {
                communication.update(SearchState.SearchHistorySuccess(list))
            }

            override fun mapSongsSuccess(list: List<SongUi>) {
                communication.update(SearchState.SongsSuccess(list))
            }

            override fun mapError(cause: String) {
                communication.update(SearchState.Error(cause))
            }
        }
    }

    class SearchSuccess(private val list: List<SearchUi>) : SearchResponse {
        override fun map(mapper: SearchResponseMapper){
            mapper.mapSearchHistorySuccess(list)
        }
    }

    class SongsSuccess(private val list: List<SongUi>) : SearchResponse {
        override fun map(mapper: SearchResponseMapper){
            mapper.mapSongsSuccess(list)
        }
    }

    class Error(private val cause: String) : SearchResponse {
        override fun map(mapper: SearchResponseMapper) {
            mapper.mapError(cause)
        }
    }
}