package com.example.tagplayer.playback_control.presentation

import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.core.Module

@UnstableApi
interface PlaybackControlModule : Module<PlaybackControlViewModel> {
    class Base : PlaybackControlModule {
        private val observable: PlaybackControlObservable = PlaybackControlObservable()
        override fun create(): PlaybackControlViewModel =
            PlaybackControlViewModel(observable)
    }
}