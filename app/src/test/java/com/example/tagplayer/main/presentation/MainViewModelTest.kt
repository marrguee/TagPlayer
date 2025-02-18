package com.example.tagplayer.main.presentation

import com.example.tagplayer.FakeAllObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.main.presentation.navigation.Screen
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var observable: FakeObservable

    @Before
    fun setup() {
        observable = FakeObservable.Base()
        viewModel = MainViewModel(observable)
    }

    @Test
    fun `main scenario`() {
        val observer = object : CustomObserver<Screen> {
            override fun update(data: Screen) = data.consumed(viewModel)
        }
        viewModel.startGettingUpdates(observer)
        observable.checkObserver(observer)

        viewModel.stopGettingUpdates()
        observable.checkObserver(MainCallback.Empty)

        viewModel.startGettingUpdates(observer)
        observable.update(Screen.Pop)
        observable.checkClearTimes(1)
    }

    private interface FakeObservable : FakeAllObservable<Screen> {
        class Base : FakeAllObservable.Base<Screen>(Screen.Empty, MainCallback.Empty),
            FakeObservable
    }
}