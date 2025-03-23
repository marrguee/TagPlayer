package com.example.tagplayer.main.presentation

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.navigation.Screen

class MainViewModel(
    private val observable: CustomObservable.All<Screen>
) : ViewModel(), HandleUiStateUpdates.All<Screen> {

    override fun startGettingUpdates(observer: CustomObserver<Screen>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(MainCallback.Empty)

    override fun clear() = observable.clear()
}