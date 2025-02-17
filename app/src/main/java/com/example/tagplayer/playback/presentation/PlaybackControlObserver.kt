package com.example.tagplayer.playback.presentation

import com.example.tagplayer.core.CustomObserver

interface PlaybackControlObserver : CustomObserver<PlayState> {
    object Empty : PlaybackControlObserver {
        override fun update(data: PlayState) = Unit
    }
}