package com.example.tagplayer.playback.domain

import kotlinx.coroutines.flow.Flow

interface HandleSongDetails {
    interface Obtain<T> {
        fun tags(id: Long): Flow<List<T>>
        suspend fun uri(id: Long) : String
    }

    interface Delete<E> {
        suspend fun deleteSong(songId: Long): E
    }

    interface Mutable<T, E> : Obtain<T>, Delete<E>
}