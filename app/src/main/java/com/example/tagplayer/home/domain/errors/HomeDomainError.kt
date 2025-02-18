package com.example.tagplayer.home.domain.errors

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.DomainError

interface HomeDomainError {
    object Recently : HomeDomainError, DomainError(R.string.failed_fetch_recently) {
        private fun readResolve(): Any = Recently
    }

    object Library : HomeDomainError, DomainError(R.string.failed_fetch_songs) {
        private fun readResolve(): Any = Library
    }
}