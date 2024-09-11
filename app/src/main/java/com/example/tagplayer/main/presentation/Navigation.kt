package com.example.tagplayer.main.presentation

import com.example.tagplayer.core.CustomObservable

interface Navigation {
    interface Navigate : CustomObservable.UpdateUi<Screen>
    interface NavHost : CustomObservable.UpdateUiObserver<Screen>, CustomObservable.Clear
    interface Mutable : Navigate, NavHost

    object Base : CustomObservable.ManualClear<Screen>(Screen.Empty), Mutable
}