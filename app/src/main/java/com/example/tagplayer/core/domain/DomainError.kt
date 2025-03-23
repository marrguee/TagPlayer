package com.example.tagplayer.core.domain

import com.example.tagplayer.R

abstract class DomainError(private val msg: Int) : Exception() {
    fun reveal() = msg

    class Unknown : DomainError(R.string.unknown_error)
}