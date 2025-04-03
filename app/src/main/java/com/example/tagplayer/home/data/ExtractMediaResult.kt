package com.example.tagplayer.home.data

import android.content.IntentSender
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.playback.data.HandleMediaResult


interface ExtractMediaResult {
    suspend fun map(mapper: Mapper): HandleMediaResult

    interface Mapper {
        suspend fun mapDeletingWithPermission(intentSender: IntentSender): HandleMediaResult
        suspend fun mapDeletingWithoutPermission(songId: Long): HandleMediaResult
        suspend fun mapAlreadyDeleted(): HandleMediaResult
        suspend fun mapError(error: String): HandleMediaResult

        class Base(private val songsDao: SongsDao): Mapper {
            override suspend fun mapDeletingWithPermission(intentSender: IntentSender): HandleMediaResult =
                HandleMediaResult.DeletingWithPermission(intentSender)

            override suspend fun mapDeletingWithoutPermission(songId: Long): HandleMediaResult {
                songsDao.deleteSong(songId)
                return HandleMediaResult.AlreadyDeleted
            }

            override suspend fun mapAlreadyDeleted(): HandleMediaResult =
                HandleMediaResult.AlreadyDeleted

            override suspend fun mapError(error: String): HandleMediaResult =
                HandleMediaResult.Error(error)
        }
    }
    class DeletingWithPermission(private val intentSender: IntentSender): ExtractMediaResult {
        override suspend fun map(mapper: Mapper) =
            mapper.mapDeletingWithPermission(intentSender)
    }

    class DeletingWithoutPermission(private val songId: Long): ExtractMediaResult {
        override suspend fun map(mapper: Mapper) =
            mapper.mapDeletingWithoutPermission(songId)
    }

    class Error(private val error: String): ExtractMediaResult {
        override suspend fun map(mapper: Mapper) =
            mapper.mapError(error)
    }

    object AlreadyDeleted: ExtractMediaResult {
        override suspend fun map(mapper: Mapper): HandleMediaResult =
            mapper.mapAlreadyDeleted()
    }
}