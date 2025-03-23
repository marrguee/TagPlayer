package com.example.tagplayer.playback.presentation

import com.example.tagplayer.core.presentation.observable.CustomObservable

class PlaybackObservable :
    CustomObservable.ManualClear<PlaybackState>(PlaybackState.Empty)