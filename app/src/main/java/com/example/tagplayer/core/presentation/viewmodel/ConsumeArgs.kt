package com.example.tagplayer.core.presentation.viewmodel

interface ConsumeArgs<T> {
    fun consume(data: T)
}