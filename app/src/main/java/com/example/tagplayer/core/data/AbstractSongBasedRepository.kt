package com.example.tagplayer.core.data

import com.example.tagplayer.core.domain.PlayForeground

abstract class AbstractSongBasedRepository(
    private val foregroundWrapper: ForegroundWrapper,
) : PlayForeground {
    override fun play(id: Long) {
        foregroundWrapper.playMedia(id)
    }
}