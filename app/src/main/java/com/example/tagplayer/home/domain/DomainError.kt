package com.example.tagplayer.home.domain

abstract class DomainError(private val msg: String) : Exception() {
    fun reveal() = msg
}
object JustError : DomainError("Just Some Errors Occurred")