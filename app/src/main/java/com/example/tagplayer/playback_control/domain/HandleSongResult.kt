package com.example.tagplayer.playback_control.domain

import android.content.IntentSender
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.playback_control.presentation.PlayState


interface HandleSongResult {
    fun map(mapper: Mapper)

    interface Mapper {
        fun mapIntentSender(intentSender: IntentSender)
        fun mapSucceed()
        fun mapError(error: String)

        class Base(
            private val observable: CustomObservable.Mutable<PlayState>
        ): Mapper {
            override fun mapIntentSender(intentSender: IntentSender) {
                observable.update(PlayState.PermissionRequired(intentSender))
            }

            override fun mapSucceed() {
                observable.update(PlayState.DeletingSuccess)
                observable.update(PlayState.DisableMotion)
            }

            override fun mapError(error: String) {
                observable.update(PlayState.Error(error))
            }
        }
    }

    class DeletingWithPermission(private val intentSender: IntentSender): HandleSongResult {
        override fun map(mapper: Mapper) {
            mapper.mapIntentSender(intentSender)
        }
    }

    object DeletionSucceed: HandleSongResult {
        override fun map(mapper: Mapper) = mapper.mapSucceed()
    }

    class Error(private val msg: String): HandleSongResult {
        override fun map(mapper: Mapper) =
            mapper.mapError(msg)
    }
}