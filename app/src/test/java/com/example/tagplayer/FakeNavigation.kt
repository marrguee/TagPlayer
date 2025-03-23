package com.example.tagplayer

import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.main.presentation.navigation.Screen
import org.junit.Assert.assertEquals

interface FakeNavigation : Navigation.Navigate {
    fun checkScreen(expected: Screen)

    class Base : FakeNavigation {
        private var screen: Screen = Screen.Empty

        override fun checkScreen(expected: Screen) {
            assertEquals(expected, screen)
        }

        override fun update(data: Screen) {
            screen = data
        }
    }
}