package com.example.tagplayer.search.domain

import com.example.tagplayer.all.presentation.SongUi
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.search.presentation.SearchUi

interface SearchResponse {
    fun map(mapper: SearchResponseMapper)

    interface SearchResponseMapper {
        fun mapHistorySuccess(list: List<SearchUi>)

        fun mapSongsSuccess(list: List<SongUi>)
        fun mapError(cause: String)

        class Base(
            private val communication: Communication<SearchState>
        ) : SearchResponseMapper {
            override fun mapHistorySuccess(list: List<SearchUi>) {
                communication.update(SearchState.HistorySuccess(list))
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
            mapper.mapHistorySuccess(list)
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