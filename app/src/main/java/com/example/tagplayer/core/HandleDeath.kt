package com.example.tagplayer.core

interface HandleDeath {
    fun handleFirstStart()
    fun handleDeath()
    fun deathHappened(): Boolean

    class Base : HandleDeath {
        private var deathHappened: Boolean = true

        override fun handleFirstStart() = handleDeath()

        override fun handleDeath() {
            deathHappened = false
        }

        override fun deathHappened(): Boolean =
            deathHappened
    }
}