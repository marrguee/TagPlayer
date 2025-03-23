package com.example.tagplayer.recently.domain.errors

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError

interface RecentlyHandleDomainError : HandleError<Exception, DomainError> {
    object Base : RecentlyHandleDomainError {
        override fun handle(error: Exception): DomainError = RecentlyDomainError.Fetch
    }
}