package com.example.tagplayer.tag_details.domain.errors

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError

interface TagDetailsHandleDomainError : HandleError<TagDetailsException, DomainError> {
    object Base : TagDetailsHandleDomainError {
        override fun handle(error: TagDetailsException): DomainError = when(error) {
            is TagDetailsException.Details -> TagDetailsDomainError.Details
            is TagDetailsException.Add -> TagDetailsDomainError.Add
            else -> DomainError.Unknown()
        }
    }
}