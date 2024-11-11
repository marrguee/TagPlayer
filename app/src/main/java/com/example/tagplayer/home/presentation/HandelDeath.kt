package com.example.tagplayer.home.presentation

interface HandelDeath {
    fun handleDeath()
    fun deathHappened(): Boolean

    class Base : HandelDeath {
        private var deathHappened: Boolean = true

        override fun handleDeath() {
            deathHappened = false
        }

        override fun deathHappened(): Boolean =
            deathHappened
    }
}