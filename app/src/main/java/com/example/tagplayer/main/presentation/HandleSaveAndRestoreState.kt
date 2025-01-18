package com.example.tagplayer.main.presentation

import com.example.tagplayer.core.HandleSaveRestoreState

interface HandleSaveAndRestoreState<T> {
    fun init(bundle: HandleSaveRestoreState.Restore<T>)
    fun save(bundle: HandleSaveRestoreState.Save<T>)
}