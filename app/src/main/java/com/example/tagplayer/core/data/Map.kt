package com.example.tagplayer.core.data

interface Map<M> {
    fun <T> map(mapper: M): T
}