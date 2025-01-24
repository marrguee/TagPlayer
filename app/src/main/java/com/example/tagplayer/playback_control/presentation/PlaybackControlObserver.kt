package com.example.tagplayer.playback_control.presentation

import com.example.tagplayer.core.CustomObserver

interface PlaybackControlObserver : CustomObserver<PlaybackState> {
    object Empty : PlaybackControlObserver {
        override fun update(data: PlaybackState) = Unit
    }
}