package com.example.tagplayer

import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert

interface FakeDispatcherList: DispatcherList {
    fun checkIoTimesCalled(expected: Int)
    fun checkUiTimesCalled(expected: Int)

    @OptIn(ExperimentalCoroutinesApi::class)
    class Base(
        private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    ): FakeDispatcherList {
        private var ioTimesCalled: Int = 0
        private var uiTimesCalled: Int = 0

        override fun checkIoTimesCalled(expected: Int) {
            Assert.assertEquals(expected, ioTimesCalled)
        }

        override fun checkUiTimesCalled(expected: Int) {
            Assert.assertEquals(expected, uiTimesCalled)
        }

        override fun io(): CoroutineDispatcher {
            ioTimesCalled++
            return dispatcher
        }

        override fun ui(): CoroutineDispatcher {
            uiTimesCalled++
            return dispatcher
        }
    }
}