package com.example.tagplayer

import com.example.tagplayer.core.presentation.HandleDeath
import org.junit.Assert.assertEquals

interface FakeHandleDeath: HandleDeath {
    fun checkFirstOpeningCalled(times: Int)
    fun checkHandleDeathCalled(times: Int)

    class Base: FakeHandleDeath {
        private var deathHappened = true
        private var handleFirstStart: Int = 0
        private var handleDeath: Int = 0

        override fun checkFirstOpeningCalled(times: Int) = assertEquals(times, handleFirstStart)

        override fun checkHandleDeathCalled(times: Int) = assertEquals(times, handleDeath)

        override fun handleFirstStart() {
            deathHappened = false
            handleFirstStart += 1
        }

        override fun handleDeath() {
            deathHappened = false
            handleDeath++
        }

        override fun deathHappened(): Boolean = deathHappened
    }
}