package com.example.tagplayer.all.domain

import com.example.tagplayer.all.data.TrackData
import com.example.tagplayer.all.presentation.AllState
import com.example.tagplayer.all.presentation.TrackUi
import com.example.tagplayer.core.Communication
import java.util.concurrent.TimeUnit

interface Response {
    fun map(mapper: Mapper)
    interface Mapper {
        fun mapSuccess(flow: List<TrackUi>)
        fun mapError(msg: String)

        class AllMapper(
            private val communication: Communication<AllState>
        ) : Mapper {
            override fun mapSuccess(flow: List<TrackUi>) {
                communication.update(AllState.TracksUpdated(flow))
            }

            override fun mapError(msg: String) {
                communication.update(AllState.Error(msg))
            }

        }
    }

    class TracksResponseSuccess(
        private val flow: List<TrackUi>
    ) : Response {
        override fun map(mapper: Mapper) {
            mapper.mapSuccess(flow)
        }
    }

    class TracksResponseError(
        private val msg: String
    ) : Response {
        override fun map(mapper: Mapper) {
            mapper.mapError(msg)
        }
    }
}