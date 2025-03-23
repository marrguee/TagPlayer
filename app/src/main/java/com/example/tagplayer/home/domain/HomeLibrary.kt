package com.example.tagplayer.home.domain

import com.example.tagplayer.home.data.ObtainFieldName
import kotlinx.coroutines.flow.Flow

interface HomeLibrary<T> {
    fun sorted(field: ObtainFieldName, order: OrderType): Flow<List<T>>
}