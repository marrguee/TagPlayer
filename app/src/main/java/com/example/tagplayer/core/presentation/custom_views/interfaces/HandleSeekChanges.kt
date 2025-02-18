package com.example.tagplayer.core.presentation.custom_views.interfaces

interface HandleSeekChanges {
    fun progress(position: Long)
    fun duration(duration: Long)
    fun enable(enable: Boolean)
}