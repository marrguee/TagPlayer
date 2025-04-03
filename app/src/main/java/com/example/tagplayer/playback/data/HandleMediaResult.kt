package com.example.tagplayer.playback.data

import android.content.IntentSender
import com.example.tagplayer.playback.domain.SongDetailsResponse

interface HandleMediaResult {
    fun map(mapper: Mapper): SongDetailsResponse

    interface Mapper {
        fun mapIntentSender(intentSender: IntentSender): SongDetailsResponse
        fun mapAlreadyDeleted(): SongDetailsResponse
        fun mapError(error: String): SongDetailsResponse

        object Base: Mapper {
            override fun mapIntentSender(intentSender: IntentSender): SongDetailsResponse =
                SongDetailsResponse.DeletingWithPermission(intentSender)

            override fun mapAlreadyDeleted(): SongDetailsResponse =
                SongDetailsResponse.DeletionSucceed

            override fun mapError(error: String): SongDetailsResponse =
                SongDetailsResponse.Error(error)
        }
    }

    class DeletingWithPermission(private val intentSender: IntentSender): HandleMediaResult {
        override fun map(mapper: Mapper): SongDetailsResponse =
            mapper.mapIntentSender(intentSender)
    }

    object AlreadyDeleted: HandleMediaResult {
        override fun map(mapper: Mapper): SongDetailsResponse =
            mapper.mapAlreadyDeleted()
    }

    class Error(private val error: String): HandleMediaResult {
        override fun map(mapper: Mapper): SongDetailsResponse =
            mapper.mapError(error)
    }
}