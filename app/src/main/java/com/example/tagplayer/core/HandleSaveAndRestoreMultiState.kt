package com.example.tagplayer.core

interface HandleSaveAndRestoreMultiState<T, E> {
    fun init(bundle: HandleSaveRestoreState.Restore<T>, bundleIn: HandleSaveRestoreState.Restore<E>)
    fun save(bundle: HandleSaveRestoreState.Save<T>, bundleIn: HandleSaveRestoreState.Save<E>)
}