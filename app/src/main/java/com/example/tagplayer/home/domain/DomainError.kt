package com.example.tagplayer.home.domain

abstract class DomainError(private val msg: String) : Exception() {
    fun reveal() = msg
}

object JustSomeError : DomainError("Just Some Errors Occurred") {
    private fun readResolve(): Any = JustSomeError
}