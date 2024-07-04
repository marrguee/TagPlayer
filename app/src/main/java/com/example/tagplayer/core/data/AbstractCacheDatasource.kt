package com.example.tagplayer.core.data

import kotlinx.coroutines.flow.Flow

abstract class AbstractCacheDatasource<T, E> {
    fun handleRequest(vararg params : T) : Flow<List<E>> = request(*params)
    protected abstract fun request(vararg params : T) : Flow<List<E>>
}