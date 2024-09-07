package com.example.tagplayer.recently.domain

import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.recently.presentation.RecentlyState
import com.example.tagplayer.recently.presentation.RecentlyUi

interface RecentlyResponse {
    fun map(mapper: HistoryResponseMapper)

    interface HistoryResponseMapper {
        fun mapSuccess(list: List<RecentlyUi>)
        fun mapError(msg: String)

        class Base(
            private val communication: Communication<RecentlyState>,
            private val dispatcherList: DispatcherList
        ) : HistoryResponseMapper {
            override fun mapSuccess(list: List<RecentlyUi>) {
                communication.update(RecentlyState.RecentlyUpdated(list))
            }

            override fun mapError(msg: String) {
                communication.update(RecentlyState.Error(msg))
            }

        }
    }

    class RecentlyResponseSuccess(
        private val list: List<RecentlyUi>
    ) : RecentlyResponse {
        override fun map(mapper: HistoryResponseMapper) {
            mapper.mapSuccess(list)
        }
    }

    class RecentlyResponseError(
        private val msg: String
    ) : RecentlyResponse {
        override fun map(mapper: HistoryResponseMapper) {
            mapper.mapError(msg)
        }
    }
}