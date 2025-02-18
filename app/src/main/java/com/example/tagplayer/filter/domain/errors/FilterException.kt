package com.example.tagplayer.filter.domain.errors

abstract class FilterException : Exception() {
    class Fetch : FilterException()
    class Save : FilterException()
    class Clear : FilterException()
}