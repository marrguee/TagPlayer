package com.example.tagplayer.core.domain

import com.example.tagplayer.core.media_service.MediaObserver

interface ProvideMediaObserver {
    fun mediaObserver(): MediaObserver
}