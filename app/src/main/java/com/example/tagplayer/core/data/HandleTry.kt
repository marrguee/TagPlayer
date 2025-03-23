package com.example.tagplayer.core.data

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError

interface HandleTry<E : Exception> {
    suspend fun <T> handleAsync(exception: E, block: suspend () -> T) : T
    fun <T> handle(exception: E, block: () -> T) : T

    class Base<E : Exception>(private val handleError: HandleError<E, DomainError>) : HandleTry<E> {
        override suspend fun <T> handleAsync(exception: E, block: suspend () -> T): T =
            try {
                block.invoke()
            } catch (e: Exception) {
                throw handleError.handle(exception)
            }

        override fun <T> handle(exception: E, block: () -> T): T = try {
            block.invoke()
        } catch (e: Exception) {
            throw handleError.handle(exception)
        }
    }
}