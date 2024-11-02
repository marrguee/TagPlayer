package com.example.tagplayer.core.domain

import com.example.tagplayer.core.CustomObserver

interface HandleUiStateUpdates<T> {

    interface StartAndStopUpdates<T> {
        fun startGettingUpdates(observer: CustomObserver<T>)
        fun stopGettingUpdates()
    }

    interface ClearObservable {
        fun clear()
    }

    interface All<T> : StartAndStopUpdates<T>, ClearObservable
}