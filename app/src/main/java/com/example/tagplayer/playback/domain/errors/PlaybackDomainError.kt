package com.example.tagplayer.playback.domain.errors

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.DomainError

interface PlaybackDomainError {
    object FetchTags : PlaybackDomainError, DomainError(R.string.failed_fetch_tags) {
        private fun readResolve(): Any = FetchTags
    }

    object Share : PlaybackDomainError, DomainError(R.string.failed_share) {
        private fun readResolve(): Any = Share
    }

    object Delete : PlaybackDomainError, DomainError(R.string.failed_delete) {
        private fun readResolve(): Any = Delete
    }
}