package com.example.tagplayer.core.domain

interface HandleTap<T> {
    fun tap(listener: (T) -> Unit)
}