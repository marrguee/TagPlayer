package com.example.tagplayer.tag_settings.domain.errors

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError

interface TagSettingsHandleDomainError : HandleError<TagSettingsException, DomainError> {
    object Base : TagSettingsHandleDomainError {
        override fun handle(error: TagSettingsException): DomainError = when(error) {
            is TagSettingsException.Fetch -> TagSettingsDomainError.Fetch
            is TagSettingsException.Remove -> TagSettingsDomainError.Remove
            else -> DomainError.Unknown()
        }
    }
}