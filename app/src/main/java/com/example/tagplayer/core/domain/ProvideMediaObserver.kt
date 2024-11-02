package com.example.tagplayer.core.domain

import com.example.tagplayer.core.MediaObserver

interface ProvideMediaObserver {
    fun mediaObserver(): MediaObserver
}