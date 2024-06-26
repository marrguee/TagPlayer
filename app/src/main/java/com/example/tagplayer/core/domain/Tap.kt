package com.example.tagplayer.core.domain

interface Tap {
    fun tap(listener: (Long) -> Unit)
}