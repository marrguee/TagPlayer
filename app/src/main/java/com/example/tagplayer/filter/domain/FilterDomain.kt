package com.example.tagplayer.filter.domain

import com.example.tagplayer.filter.presentation.FilterUi

data class FilterDomain(
    private val id: Long,
    private val title: String,
    private val color: String,
    private val selected: Boolean,
) {
    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, title, color, selected)

    interface Mapper<T> {
        fun map(id: Long, title: String, color: String, selected: Boolean): T

        object Ui : Mapper<FilterUi> {
            override fun map(id: Long, title: String, color: String, selected: Boolean): FilterUi =
                FilterUi(id, title, color, selected)
        }
    }
}
