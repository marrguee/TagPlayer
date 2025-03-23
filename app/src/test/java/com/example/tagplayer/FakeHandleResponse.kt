package com.example.tagplayer

import com.example.tagplayer.core.domain.HandleResponse
import org.junit.Assert.assertEquals

interface FakeHandleResponse : HandleResponse {
    
    interface Handle<T> : HandleResponse.Handle<T> {
        fun checkHandleCalled(times: Int)
        fun checkHandleAsyncCalled(times: Int)
    }

    interface HandleEmpty<T> : HandleResponse.HandleEmpty<T> {
        fun checkHandleAsyncEmptyCalled(times: Int)
    }

    interface All<T> : Handle<T>, HandleEmpty<T>, HandleResponse.All<T>
    
    open class Base<T> : Handle<T> {
        private var handleCalled = 0
        private var handleAsyncCalled = 0

        override fun checkHandleCalled(times: Int) = assertEquals(times, handleCalled)

        override fun checkHandleAsyncCalled(times: Int) = assertEquals(times, handleAsyncCalled)

        override fun handle(block: () -> T): T {
            handleCalled++
            return block.invoke()
        }

        override suspend fun handleAsync(block: suspend () -> T): T {
            handleAsyncCalled++
            return block.invoke()
        }
    }

    class Empty<T>(private val empty: T) : Base<T>(), All<T> {
        private var handleAsyncEmptyCalled = 0
        
        override fun checkHandleAsyncEmptyCalled(times: Int) =
            assertEquals(times, handleAsyncEmptyCalled)
        
        override suspend fun handleAsyncEmpty(block: suspend () -> Unit): T {
            handleAsyncEmptyCalled++
            block.invoke()
            return empty
        }
    }
}