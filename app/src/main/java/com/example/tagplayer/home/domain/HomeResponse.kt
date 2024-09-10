package com.example.tagplayer.home.domain

import com.example.tagplayer.home.presentation.HomeState
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.flow.Flow

interface HomeResponse {
    fun map(mapper: HomeResponseMapper)

    interface HomeResponseMapper {
        fun mapRecentlySuccess(list: List<SongUi>)
        fun mapLibrarySuccess(flow: Flow<List<SongUi>>)
        fun mapError(msg: String)

        class Base(
            private val communication: Communication<HomeState>,
            private val dispatcherList: DispatcherList
        ) : HomeResponseMapper {

            override fun mapRecentlySuccess(list: List<SongUi>) {
                communication.update(HomeState.RecentlyUpdated(list))
            }

            override fun mapLibrarySuccess(flow: Flow<List<SongUi>>) {
//                coroutineScope.launch {
//                    flow.flowOn(dispatcherList.io()).collect { songs ->
//                        communication.update(HomeState.LibraryUpdated(songs))
//                    }
//                }
            }

            override fun mapError(msg: String) {
                communication.update(HomeState.Error(msg))
            }

        }
    }

    class RecentlyHomeResponseSuccess(
        private val list: List<SongUi>
    ) : HomeResponse {
        override fun map(mapper: HomeResponseMapper) {
            mapper.mapRecentlySuccess(list)
        }
    }

    class LibraryHomeResponseSuccess(
        private val flow: Flow<List<SongUi>>
    ) : HomeResponse {
        override fun map(mapper: HomeResponseMapper) {
            //mapper.mapLibrarySuccess(flow, coroutineScope)
        }
    }

    class HomeResponseError(
        private val msg: String
    ) : HomeResponse {
        override fun map(mapper: HomeResponseMapper) {
            mapper.mapError(msg)
        }
    }
}