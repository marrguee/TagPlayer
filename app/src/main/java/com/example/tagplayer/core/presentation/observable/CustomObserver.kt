package com.example.tagplayer.core.presentation.observable

interface CustomObserver<T> {
    fun update(data: T)

    class Empty<T> : CustomObserver<T> {
        override fun update(data: T) = Unit
    }
}