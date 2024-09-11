package com.example.tagplayer.core

interface CustomObserver<T> {
    fun update(data: T)

    class Empty<T> : CustomObserver<T> {
        override fun update(data: T) = Unit
    }
}