package com.example.tagplayer

import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals

interface FakeRunAsync : RunAsync {
    fun checkHandleCalled(times: Int)
    fun checkHandleMultiplyCalled(times: Int)
    fun pingResult()

    @Suppress("UNCHECKED_CAST")
    class Base : FakeRunAsync {
        private var handleCalled: Int = 0
        private var handleMultiplyCalled: Int = 0
        private var cachedFunUi: (Any) -> Unit = {}
        private var cache: Any = Any()

        override fun checkHandleCalled(times: Int) = assertEquals(times, handleCalled)

        override fun checkHandleMultiplyCalled(times: Int) =
            assertEquals(times, handleMultiplyCalled)

        override fun pingResult() = cachedFunUi.invoke(cache)

        override fun <T : Any> handle(
            scope: CoroutineScope,
            uiBlock: (T) -> Unit,
            block: suspend () -> T
        ) {
            handleCalled++
            handleMultiply(scope, false, uiBlock, block)
        }

        override fun <T : Any> handleMultiply(
            scope: CoroutineScope,
            flag: Boolean,
            uiBlock: (T) -> Unit,
            block: suspend () -> T
        ) = runBlocking {
            if (!flag) handleMultiplyCalled++
            cache = block.invoke()
            cachedFunUi = uiBlock as (Any) -> Unit
        }
    }
}