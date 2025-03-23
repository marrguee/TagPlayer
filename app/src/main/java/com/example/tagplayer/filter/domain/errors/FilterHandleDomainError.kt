package com.example.tagplayer.filter.domain.errors

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError

interface FilterHandleDomainError : HandleError<FilterException, DomainError> {
    object Base : FilterHandleDomainError {
        override fun handle(error: FilterException): DomainError = when(error) {
            is FilterException.Fetch -> FilterDomainError.Fetch
            is FilterException.Save -> FilterDomainError.Save
            is FilterException.Clear -> FilterDomainError.Clear
            else -> DomainError.Unknown()
        }
    }
}