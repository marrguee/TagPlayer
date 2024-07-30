package com.example.tagplayer.main

import com.example.tagplayer.core.Clear
import com.example.tagplayer.core.ObservableUi
import com.example.tagplayer.core.UpdateUi
import com.example.tagplayer.core.UpdateUiObserver
import com.example.tagplayer.main.presentation.Screen

interface Navigation {
    interface Navigate : UpdateUi<Screen>
    interface NavHost : UpdateUiObserver<Screen>, Clear
    interface Mutable : Navigate, NavHost

    object Base : ObservableUi.ManualClear<Screen>(Screen.Empty), Mutable
}