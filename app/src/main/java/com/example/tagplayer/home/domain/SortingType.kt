package com.example.tagplayer.home.domain

import com.example.tagplayer.home.data.SortingDataType

interface SortingType {
    fun map(tags: List<Long> = emptyList()): SortingDataType

    object SortTitleAsc: SortingType {
        override fun map(tags: List<Long>): SortingDataType =
            if (tags.isEmpty()) SortingDataType.SortTitleAsc
            else SortingDataType.SortTagsTitleAsc(tags)
    }

    object SortTitleDesc: SortingType {
        override fun map(tags: List<Long>): SortingDataType =
            if(tags.isEmpty()) SortingDataType.SortTitleDesc
            else SortingDataType.SortTagsTitleDesc(tags)
    }

    object SortDateAsc: SortingType {
        override fun map(tags: List<Long>): SortingDataType =
            if(tags.isEmpty()) SortingDataType.SortDateAsc
            else SortingDataType.SortTagsDateAsc(tags)
    }

    object SortDateDesc: SortingType {
        override fun map(tags: List<Long>): SortingDataType =
            if(tags.isEmpty()) SortingDataType.SortDateDesc
            else SortingDataType.SortTagsDateDesc(tags)
    }
}