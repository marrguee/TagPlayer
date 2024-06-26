package com.example.tagplayer.history.domain

abstract class DomainException : Exception() {
    abstract fun message() : String
}