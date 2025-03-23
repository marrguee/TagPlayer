package com.example.tagplayer.playback.presentation

import com.example.tagplayer.core.presentation.observable.CustomObserver

interface PlaybackObserver : CustomObserver<PlaybackState> {
    object Empty : PlaybackObserver {
        override fun update(data: PlaybackState) = Unit
    }
}