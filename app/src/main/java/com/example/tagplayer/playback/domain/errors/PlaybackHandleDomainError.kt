package com.example.tagplayer.playback.domain.errors

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError

interface PlaybackHandleDomainError : HandleError<PlaybackCustomException, DomainError> {
    object Base : PlaybackHandleDomainError {
        override fun handle(error: PlaybackCustomException): DomainError = when(error) {
            is PlaybackCustomException.FetchTagsException -> PlaybackDomainError.FetchTags
            is PlaybackCustomException.ShareException -> PlaybackDomainError.Share
            is PlaybackCustomException.DeleteException -> PlaybackDomainError.Delete
            else -> DomainError.Unknown()
        }
    }
}