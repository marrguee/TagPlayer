package com.example.tagplayer.home.domain.errors

import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.filter.domain.errors.FilterHandleDomainError

interface HomeHandleDomainError : HandleError<HomeException, DomainError> {
    object Base : HomeHandleDomainError {
        override fun handle(error: HomeException): DomainError = when(error) {
            is HomeException.Library -> HomeDomainError.Library
            is HomeException.Recently -> HomeDomainError.Recently
            else -> DomainError.Unknown()
        }
    }
}