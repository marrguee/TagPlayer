package com.example.tagplayer.core

interface SaveAndRestoreParcelable<T> {
    fun save(): T
    fun restore(data: T)
}