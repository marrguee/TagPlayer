package com.example.tagplayer.tags_attach.domain.errors

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError

interface AttachHandleDomainError : HandleError<AttachTagsException, DomainError> {
    object Base : AttachHandleDomainError {
        override fun handle(error: AttachTagsException): DomainError = when(error) {
            is AttachTagsException.All -> AttachTagsDomainError.All()
            is AttachTagsException.Owned -> AttachTagsDomainError.Owned()
            is AttachTagsException.Add -> AttachTagsDomainError.Add()
            is AttachTagsException.Remove -> AttachTagsDomainError.Remove()
            else -> DomainError.Unknown()
        }
    }
}