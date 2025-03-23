package com.example.tagplayer.home.domain.errors

abstract class HomeException : Exception() {
    class Recently : HomeException()
    class Library : HomeException()
}