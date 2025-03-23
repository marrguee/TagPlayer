package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.presentation.observable.CustomObserver

interface HomeObserver : CustomObserver<HomeState> {

    object Empty : HomeObserver {
        override fun update(data: HomeState) = Unit
    }
}