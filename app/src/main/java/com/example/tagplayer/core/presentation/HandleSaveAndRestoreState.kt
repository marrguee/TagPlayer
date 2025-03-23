package com.example.tagplayer.core.presentation

interface HandleSaveAndRestoreState<T> {
    fun init(bundle: HandleSaveRestoreState.Restore<T>)
    fun save(bundle: HandleSaveRestoreState.Save<T>)
}