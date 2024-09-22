package com.example.tagplayer.core.domain

import com.example.tagplayer.core.CustomObserver

interface HandleUiStateUpdates<T> {

    interface StartAndStopUpdates<T> {
        fun startGettingUpdates(observer: CustomObserver<T>)
        fun stopGettingUpdates()
    }

    interface ClearObserver {
        fun clearObserver()
    }

    interface All<T> : StartAndStopUpdates<T>, ClearObserver
}