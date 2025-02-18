package com.example.tagplayer.playback.presentation

import androidx.media3.common.Player.Listener

interface PlayerListener : Listener {
    object Empty : PlayerListener
}