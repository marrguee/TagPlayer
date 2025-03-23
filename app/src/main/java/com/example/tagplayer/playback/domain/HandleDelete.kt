package com.example.tagplayer.playback.domain

import android.content.IntentSender
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.playback.presentation.PlaybackState
import com.example.tagplayer.playback.presentation.PlaybackState.*

interface HandleDelete {
    fun map(mapper: Mapper)

    interface Mapper {
        fun mapIntentSender(intentSender: IntentSender)
        fun mapSucceed()
        fun mapError(error: String)

        class Base(private val observable: CustomObservable.Mutable<PlaybackState>): Mapper {
            override fun mapIntentSender(intentSender: IntentSender) =
                observable.update(PermissionRequired(intentSender))

            override fun mapSucceed() = observable.update(DeletingSuccess)

            override fun mapError(error: String) = observable.update(PlaybackState.Error(error))
        }
    }

    class DeletingWithPermission(private val intentSender: IntentSender): HandleDelete {
        override fun map(mapper: Mapper) = mapper.mapIntentSender(intentSender)
    }

    object DeletionSucceed: HandleDelete {
        override fun map(mapper: Mapper) = mapper.mapSucceed()
    }

    class Error(private val msg: String): HandleDelete {
        override fun map(mapper: Mapper) = mapper.mapError(msg)
    }
}