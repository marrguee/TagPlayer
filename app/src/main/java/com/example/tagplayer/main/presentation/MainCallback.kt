package com.example.tagplayer.main.presentation

import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.main.presentation.navigation.Screen

interface MainCallback : CustomObserver<Screen> {
    object Empty : MainCallback {
        override fun update(data: Screen) = Unit
    }
}