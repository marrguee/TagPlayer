package com.example.tagplayer.playback_control.presentation

import com.example.tagplayer.core.CustomObservable

class PlaybackControlObservable :
    CustomObservable.ManualClear<PlaybackControlState>(PlaybackControlState.Empty)