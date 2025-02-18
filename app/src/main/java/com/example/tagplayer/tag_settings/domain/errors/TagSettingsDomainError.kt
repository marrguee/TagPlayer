package com.example.tagplayer.tag_settings.domain.errors

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.DomainError

interface TagSettingsDomainError {
    object Fetch : TagSettingsDomainError, DomainError(R.string.failed_fetch_tags) {
        private fun readResolve(): Any = Fetch
    }

    object Remove : TagSettingsDomainError, DomainError(R.string.failed_remove_tag) {
        private fun readResolve(): Any = Remove
    }
}