package com.example.tagplayer.home.data

import com.example.tagplayer.core.data.database.models.Song
import kotlinx.coroutines.flow.Flow

interface SortingDataType {
    fun map(handleSort: HandleSort): Flow<List<Song>>

    object SortTitleAsc: SortingDataType {
        override fun map(handleSort: HandleSort) =
            handleSort.sortTitleAsc()
    }

    object SortTitleDesc: SortingDataType {
        override fun map(handleSort: HandleSort) =
            handleSort.sortTitleDesc()
    }

    object SortDateAsc: SortingDataType {
        override fun map(handleSort: HandleSort) =
            handleSort.sortDateAsc()
    }

    object SortDateDesc: SortingDataType {
        override fun map(handleSort: HandleSort) =
            handleSort.sortDateDesc()
    }

    class SortTagsTitleAsc(private val tags: List<Long>): SortingDataType {
        override fun map(handleSort: HandleSort) =
            handleSort.sortTagsAscTitle(tags)
    }

    class SortTagsTitleDesc(private val tags: List<Long>): SortingDataType {
        override fun map(handleSort: HandleSort) =
            handleSort.sortTagsTitleDesc(tags)
    }

    class SortTagsDateAsc(private val tags: List<Long>): SortingDataType {
        override fun map(handleSort: HandleSort) =
            handleSort.sortTagsDateAsc(tags)
    }

    class SortTagsDateDesc(private val tags: List<Long>): SortingDataType {
        override fun map(handleSort: HandleSort) =
            handleSort.sortTagsDateDesc(tags)
    }
}