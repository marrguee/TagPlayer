package com.example.tagplayer.search.domain

import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.search.presentation.SearchState
import com.example.tagplayer.search.presentation.SearchUi

interface SearchResponse {
    fun map(mapper: Mapper)

    interface Mapper {
        fun mapSongsSuccess(list: List<SearchUi>)
        fun mapError(cause: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<SearchState>
        ) : Mapper {

            override fun mapSongsSuccess(list: List<SearchUi>) {
                observable.update(SearchState.SongsSuccess(list))
            }

            override fun mapError(cause: String) {
                observable.update(SearchState.Error(cause))
            }
        }
    }

    class SongsSuccess(private val list: List<SearchUi>) : SearchResponse {
        override fun map(mapper: Mapper){
            mapper.mapSongsSuccess(list)
        }
    }

    class Error(private val cause: String) : SearchResponse {
        override fun map(mapper: Mapper) {
            mapper.mapError(cause)
        }
    }
}