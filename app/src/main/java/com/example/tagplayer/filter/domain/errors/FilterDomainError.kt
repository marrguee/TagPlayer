package com.example.tagplayer.filter.domain.errors

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.DomainError

interface FilterDomainError {
    object Fetch : FilterDomainError, DomainError(R.string.failed_fetch_filters) {
        private fun readResolve(): Any = Fetch
    }

    object Save : FilterDomainError, DomainError(R.string.failed_save_filter) {
        private fun readResolve(): Any = Save
    }

    object Clear : FilterDomainError, DomainError(R.string.failed_clear_filter) {
        private fun readResolve(): Any = Clear
    }
}