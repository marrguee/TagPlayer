package com.example.tagplayer.main.presentation

import com.example.tagplayer.core.ObservableUi

interface Navigation {
    interface Navigate : ObservableUi.UpdateUi<Screen>
    interface NavHost : ObservableUi.UpdateUiObserver<Screen>, ObservableUi.Clear
    interface Mutable : Navigate, NavHost

    object Base : ObservableUi.ManualClear<Screen>(Screen.Empty), Mutable
}