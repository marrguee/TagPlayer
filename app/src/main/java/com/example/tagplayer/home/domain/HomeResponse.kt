package com.example.tagplayer.home.domain

import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.home.presentation.HomeState
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.flow.Flow

interface HomeResponse {
    fun map(mapper: HomeResponseMapper)

    interface HomeResponseMapper {
        fun mapRecentlySuccess(list: List<SongUi>)
        fun mapError(msg: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<HomeState>
        ) : HomeResponseMapper {

            override fun mapRecentlySuccess(list: List<SongUi>) {
                if (list.isEmpty()) observable.update(HomeState.HideRecently)
                else observable.update(HomeState.RecentlyUpdated(list))
            }

            override fun mapError(msg: String) {
                observable.update(HomeState.Error(msg))
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

    class HomeResponseError(
        private val msg: String
    ) : HomeResponse {
        override fun map(mapper: HomeResponseMapper) {
            mapper.mapError(msg)
        }
    }
}