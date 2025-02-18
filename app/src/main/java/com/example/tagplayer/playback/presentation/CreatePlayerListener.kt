package com.example.tagplayer.playback.presentation

import androidx.media3.common.Player.Listener
import androidx.media3.session.MediaController

interface CreatePlayerListener {
    fun listener(controller: MediaController) : Listener
}