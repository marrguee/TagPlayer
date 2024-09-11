package com.example.tagplayer.core.domain

interface HandleTap {
    fun tap(listener: (Long) -> Unit)
}