package com.example.tagplayer.core.data

import kotlinx.coroutines.flow.Flow

interface HandleRepositoryRequest<O, P> {
    fun handleRequest(vararg params: P) : Flow<List<O>>
}