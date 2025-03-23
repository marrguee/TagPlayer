package com.example.tagplayer

import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

interface FakeDispatcherList: DispatcherList {

    class Base(
        private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
    ): FakeDispatcherList {

        override fun io(): CoroutineDispatcher = dispatcher

        override fun ui(): CoroutineDispatcher = dispatcher
    }
}