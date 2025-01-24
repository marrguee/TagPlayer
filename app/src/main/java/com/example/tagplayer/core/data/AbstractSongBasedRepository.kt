package com.example.tagplayer.core.data

abstract class AbstractSongBasedRepository(
    private val foregroundWrapper: ForegroundWrapper,
) {
    fun playSongForeground(id: Long) {
        foregroundWrapper.playMedia(id)
    }
}