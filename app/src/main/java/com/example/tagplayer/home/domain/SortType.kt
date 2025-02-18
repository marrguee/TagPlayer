package com.example.tagplayer.home.domain

import com.example.tagplayer.home.data.ObtainFieldName
import com.example.tagplayer.home.presentation.SongUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface SortType {

    interface Compare {
        fun compare(position: Int) : Boolean
    }

    interface Map {
        fun <T> map(mapper: Mapper<T>) : Flow<List<T>>
    }

    interface All : Compare, Map

    interface Mapper<T> {
        fun map(field: ObtainFieldName, order: OrderType) : Flow<List<T>>

        class Base(
            private val repository: HomeRepository<SongDomain>,
            private val mapper: SongDomain.Mapper.Presentation,
        ) : Mapper<SongUi> {
            override fun map(field: ObtainFieldName, order: OrderType): Flow<List<SongUi>> =
                repository.sorted(field, order).map { list -> list.map { it.map(mapper) } }
        }
    }

    abstract class Base(private val menuPosition: Int) : All {
        override fun compare(position: Int): Boolean  = menuPosition == position
    }

    object DateDesc : Base(0) {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(ObtainFieldName.SongDate, OrderType.Desc)
    }

    object DateAsc : Base(1) {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(ObtainFieldName.SongDate, OrderType.Asc)
    }

    object TitleAsc : Base(2) {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(ObtainFieldName.SongTitle, OrderType.Asc)
    }

    object TitleDesc : Base(3) {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(ObtainFieldName.SongTitle, OrderType.Desc)
    }

    object Empty : Map {
        override fun <T> map(mapper: Mapper<T>): Flow<List<T>> = flowOf()
    }
}