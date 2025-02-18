package com.example.tagplayer.core.domain

import android.content.Context
import androidx.core.content.ContextCompat

interface HandleError<E : Exception, R> {
    fun handle(error: E) : R

    class Presentation(private val context: Context) : HandleError<DomainError, String> {
        override fun handle(error: DomainError) = ContextCompat.getString(context, error.reveal())
    }
}