package com.example.tagplayer.recently.domain.errors

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.DomainError

interface RecentlyDomainError {
    object Fetch : RecentlyDomainError, DomainError(R.string.failed_fetch_recently) {
        private fun readResolve(): Any = Fetch
    }
}