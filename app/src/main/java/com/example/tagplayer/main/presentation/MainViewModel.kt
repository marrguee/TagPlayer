package com.example.tagplayer.main.presentation

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.ConsumeState
import com.example.tagplayer.core.MutableGettingUpdates
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver

class MainViewModel(
    private val observable: CustomObservable.All<Screen>
) : ViewModel(), ConsumeState, MutableGettingUpdates<Screen> {


    override fun consumeState() {
        observable.clear()
    }

    override fun startGettingUpdates(observer: CustomObserver<Screen>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.clear()
    }
}