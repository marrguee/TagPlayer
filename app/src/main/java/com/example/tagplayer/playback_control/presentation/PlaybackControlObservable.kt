package com.example.tagplayer.playback_control.presentation

import com.example.tagplayer.core.CustomObservable

class PlaybackControlObservable :
    CustomObservable.ManualClear<PlayState>(PlayState.Empty)