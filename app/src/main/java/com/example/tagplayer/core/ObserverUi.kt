package com.example.tagplayer.core

interface ObserverUi<T> {
    fun update(data: T)

    class Empty<T> : ObserverUi<T> {
        override fun update(data: T) = Unit
    }
}