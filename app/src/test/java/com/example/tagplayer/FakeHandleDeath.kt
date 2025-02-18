package com.example.tagplayer

import com.example.tagplayer.core.HandleDeath
import org.junit.Assert

interface FakeHandleDeath: HandleDeath {
    fun checkFirstOpeningCalled(expected: Int)

    class Base: FakeHandleDeath {
        private var deathHappened = true
        private var times: Int = 0

        override fun checkFirstOpeningCalled(expected: Int) {
            Assert.assertEquals(expected, times)
        }

        override fun handleFirstStart() {
            deathHappened = false
            times += 1
        }

        override fun handleDeath() {
            deathHappened = false
        }

        override fun deathHappened(): Boolean = deathHappened
    }
}