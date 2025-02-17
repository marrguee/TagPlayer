package com.example.tagplayer.playback.presentation

import com.example.tagplayer.core.CustomObservable

class PlaybackControlObservable :
    CustomObservable.ManualClear<PlayState>(PlayState.Empty)