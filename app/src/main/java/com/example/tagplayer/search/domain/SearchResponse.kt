package com.example.tagplayer.search.domain

import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.search.presentation.SongSearchUi

interface SearchResponse {
    fun map(mapper: SearchResponseMapper)

    interface SearchResponseMapper {
        fun mapSongsSuccess(list: List<SongSearchUi>)
        fun mapError(cause: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<SearchState>,
            private val dispatcherList: DispatcherList
        ) : SearchResponseMapper {

            override fun mapSongsSuccess(list: List<SongSearchUi>) {
                observable.update(SearchState.SongsSuccess(list))
            }

            override fun mapError(cause: String) {
                observable.update(SearchState.Error(cause))
            }
        }
    }

    class SongsSuccess(private val list: List<SongSearchUi>) : SearchResponse {
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