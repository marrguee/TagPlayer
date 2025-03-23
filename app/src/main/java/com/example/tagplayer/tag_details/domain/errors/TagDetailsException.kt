package com.example.tagplayer.tag_details.domain.errors

abstract class TagDetailsException : Exception() {
    class Details : TagDetailsException()
    class Add : TagDetailsException()
}