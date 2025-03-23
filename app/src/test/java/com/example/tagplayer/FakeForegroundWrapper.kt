package com.example.tagplayer

import com.example.tagplayer.core.data.ForegroundWrapper
import org.junit.Assert.assertEquals

interface FakeForegroundWrapper : ForegroundWrapper {
    fun checkScanCalled(times: Int)
    fun checkPlayCalled(times: Int)
    fun checkFetchCalled(times: Int)
    fun checkPlayCalledWithId(id: Long)

    class Base : FakeForegroundWrapper {
        private var scanCalled = 0
        private var playCalled = 0
        private var fetchCalled = 0
        private var cachedId = 0L

        override fun checkScanCalled(times: Int) {
            assertEquals(times, scanCalled)
        }

        override fun checkPlayCalled(times: Int) {
            assertEquals(times, playCalled)
        }

        override fun checkFetchCalled(times: Int) {
            assertEquals(times, fetchCalled)
        }

        override fun checkPlayCalledWithId(id: Long) {
            assertEquals(id, cachedId)
        }

        override fun scanMedia() {
            scanCalled++
        }

        override fun playMedia(id: Long) {
            cachedId = id
            playCalled++
        }

        override fun fetchNewSong(uri: String) {
            fetchCalled++
        }
    }
}