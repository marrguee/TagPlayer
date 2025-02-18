package com.example.tagplayer.core.presentation.fragments

interface ArgumentType<T> {
    fun type(): Class<T>

    object LongType : ArgumentType<Long> {
        override fun type(): Class<Long> = Long::class.java
    }
}