package com.example.tagplayer

import com.example.tagplayer.core.data.HandleTry
import org.junit.Assert.assertEquals

interface FakeHandleTry<E : Exception> : HandleTry<E> {
    fun checkHandleAsyncCalled(times: Int)
    fun checkHandleCalled(times: Int)

    abstract class Common<E : Exception> : FakeHandleTry<E> {
        private var handleAsyncCalled = 0
        private var handleCalled = 0

        override fun checkHandleAsyncCalled(times: Int) = assertEquals(times, handleAsyncCalled)

        override fun checkHandleCalled(times: Int) = assertEquals(times, handleCalled)

        override suspend fun <T> handleAsync(exception: E, block: suspend () -> T): T {
            handleAsyncCalled++
            return block.invoke()
        }


        override fun <T> handle(exception: E, block: () -> T): T {
            handleCalled++
            return block.invoke()
        }
    }

    class Base<E : Exception> : Common<E>()

    class Error<E : Exception> : Common<E>() {

        override suspend fun <T> handleAsync(exception: E, block: suspend () -> T): T =
            throw exception

        override fun <T> handle(exception: E, block: () -> T): T =
            throw exception
    }
}