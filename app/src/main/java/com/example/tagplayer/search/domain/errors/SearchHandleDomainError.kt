package com.example.tagplayer.search.domain.errors

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError

interface SearchHandleDomainError : HandleError<Exception, DomainError> {
    object Base : SearchHandleDomainError {
        override fun handle(error: Exception): DomainError = SearchDomainError.Search
    }
}