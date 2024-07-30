package com.example.tagplayer.main.presentation

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.ConsumeState
import com.example.tagplayer.core.MutableGettingUpdates
import com.example.tagplayer.core.ObservableUi
import com.example.tagplayer.core.ObserverUi

class MainViewModel(
    private val observable: ObservableUi<Screen>
) : ViewModel(), ConsumeState, MutableGettingUpdates<Screen> {


    override fun consumeState() {
        observable.clear()
    }

    override fun startGettingUpdates(observer: ObserverUi<Screen>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.clear()
    }
}