package com.example.tagplayer.search.domain.errors

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.DomainError

interface SearchDomainError {
    object Search : SearchDomainError, DomainError(R.string.failed_search) {
        private fun readResolve(): Any = Search
    }
}