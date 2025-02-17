package com.example.tagplayer.home.data

import android.content.IntentSender
import com.example.tagplayer.playback.domain.HandleSongResult


interface HandleMediaResult {
    fun map(mapper: Mapper): HandleSongResult

    interface Mapper {
        fun mapIntentSender(intentSender: IntentSender): HandleSongResult
        fun mapAlreadyDeleted(): HandleSongResult
        fun mapError(error: String): HandleSongResult

        object Base: Mapper {
            override fun mapIntentSender(intentSender: IntentSender): HandleSongResult =
                HandleSongResult.DeletingWithPermission(intentSender)

            override fun mapAlreadyDeleted(): HandleSongResult =
                HandleSongResult.DeletionSucceed

            override fun mapError(error: String): HandleSongResult =
                HandleSongResult.Error(error)
        }
    }

    class DeletingWithPermission(private val intentSender: IntentSender): HandleMediaResult {
        override fun map(mapper: Mapper): HandleSongResult =
            mapper.mapIntentSender(intentSender)
    }

    object AlreadyDeleted: HandleMediaResult {
        override fun map(mapper: Mapper): HandleSongResult =
            mapper.mapAlreadyDeleted()
    }

    class Error(private val error: String): HandleMediaResult {
        override fun map(mapper: Mapper): HandleSongResult =
            mapper.mapError(error)
    }
}