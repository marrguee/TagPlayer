package com.example.tagplayer.home.domain

interface HandleError<E, R> {
    fun handle(error: E) : R

    object Domain : HandleError<Exception, DomainError> {
        override fun handle(error: Exception): DomainError {
            return JustError
        }
    }

    object Presentation : HandleError<DomainError, String> {
        override fun handle(error: DomainError) = error.reveal()
    }

}