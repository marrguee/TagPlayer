package com.example.tagplayer.home.presentation

import android.os.Parcelable
import com.example.tagplayer.filter_by_tags.presentation.FilterUi
import com.example.tagplayer.home.domain.SortingType
import kotlinx.parcelize.Parcelize

interface TagFiltersState : Parcelable {

    fun map(mapper: TagFilterMapper, sortingType: SortingType)
    fun mapIntoAllList(allList: List<FilterUi>)

    @Parcelize
    object Empty : TagFiltersState {
        override fun map(mapper: TagFilterMapper, sortingType: SortingType) = Unit
        override fun mapIntoAllList(allList: List<FilterUi>) = Unit
    }

    @Parcelize
    object EmptyList : TagFiltersState {
        override fun map(mapper: TagFilterMapper, sortingType: SortingType) =
            mapper.mapEmptyList(sortingType)
        override fun mapIntoAllList(allList: List<FilterUi>) = Unit
    }

    @Parcelize
    class FilledList(
        private val list: List<Long>
    ) : TagFiltersState {
        override fun map(mapper: TagFilterMapper, sortingType: SortingType) =
            mapper.mapFilledList(list, sortingType)
        override fun mapIntoAllList(allList: List<FilterUi>) {
            allList.forEach { tag ->
                list.forEach { id ->
                    if (tag.compare(id)){
                        tag.changeSelected()
                    }
                }
            }
        }
    }

    @Parcelize
    class Error(private val error: String) : TagFiltersState {
        override fun map(mapper: TagFilterMapper, sortingType: SortingType) = mapper.mapError(error)
        override fun mapIntoAllList(allList: List<FilterUi>) = Unit
    }
}

