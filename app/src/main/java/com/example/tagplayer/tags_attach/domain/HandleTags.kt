package com.example.tagplayer.tags_attach.domain

import kotlinx.coroutines.flow.Flow

interface HandleTags {
    interface Obtain<T> {
        fun all(songId: Long): Flow<List<T>>
        fun owned(songId: Long): Flow<List<T>>
    }

    interface Add {
        suspend fun add(songId: Long, tagId: Long)
    }

    interface Remove {
        suspend fun remove(songId: Long, tagId: Long)
    }

    interface Mutable : Add, Remove

    interface All<T> : Obtain<T>, Mutable
}