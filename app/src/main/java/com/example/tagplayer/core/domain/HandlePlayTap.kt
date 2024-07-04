package com.example.tagplayer.core.domain

interface HandlePlayTap {
    fun tap(listener: (Long) -> Unit)
}