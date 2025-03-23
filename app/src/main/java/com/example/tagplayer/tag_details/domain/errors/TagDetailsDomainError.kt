package com.example.tagplayer.tag_details.domain.errors

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.DomainError

interface TagDetailsDomainError {
    object Details : TagDetailsDomainError, DomainError(R.string.failed_tag_details) {
        private fun readResolve(): Any = Details
    }

    object Add : TagDetailsDomainError, DomainError(R.string.failed_add_tag) {
        private fun readResolve(): Any = Add
    }
}