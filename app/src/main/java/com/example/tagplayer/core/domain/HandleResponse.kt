package com.example.tagplayer.core.domain

interface HandleResponse {

    interface Handle<R> {
        fun handle(block: () -> R) : R
        suspend fun handleAsync(block: suspend () -> R) : R
    }

    interface HandleEmpty<R> {
        suspend fun handleAsyncEmpty(block: suspend () -> Unit) : R
    }

    interface All<R> : Handle<R>, HandleEmpty<R>

    open class WithoutEmpty<R>(
        private val handleError: HandleError<DomainError, String>,
        private val errorResponse: (DomainError, HandleError<DomainError, String>) -> R
    ) : Handle<R> {

        override fun handle(block: () -> R): R = try {
            block.invoke()
        } catch (e: DomainError) {
            errorResponse.invoke(e, handleError)
        }

        override suspend fun handleAsync(block: suspend () -> R): R = try {
            block.invoke()
        } catch (e: DomainError) {
            errorResponse.invoke(e, handleError)
        }
    }

    class WithEmpty<R>(
        private val empty: R,
        handleError: HandleError<DomainError, String>,
        errorResponse: (DomainError, HandleError<DomainError, String>) -> R
    ) : WithoutEmpty<R>(handleError, errorResponse), All<R> {

        override suspend fun handleAsyncEmpty(block: suspend () -> Unit): R = handleAsync {
            block.invoke()
            empty
        }
    }
}