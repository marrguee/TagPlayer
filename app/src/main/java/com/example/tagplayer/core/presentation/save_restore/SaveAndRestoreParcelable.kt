package com.example.tagplayer.core.presentation.save_restore

interface SaveAndRestoreParcelable<T> {
    fun save(): T
    fun restore(data: T)
}