package com.example.tagplayer.playback.presentation

interface PlayerCommands : Seek {
    fun playPause()
    fun rewind()
    fun clearMediaQueue()
}