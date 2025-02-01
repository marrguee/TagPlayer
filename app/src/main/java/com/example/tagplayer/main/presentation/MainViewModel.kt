package com.example.tagplayer.main.presentation

import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.home.presentation.HomeScreen

class MainViewModel(
    private val observable: CustomObservable.All<Screen>
) : ViewModel(), HandleUiStateUpdates.All<Screen> {

    override fun startGettingUpdates(observer: CustomObserver<Screen>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(MainActivityCallback.Empty)
    }

    override fun clear() {
        observable.clear()
    }
}