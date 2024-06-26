package com.example.tagplayer.core.domain

interface CompareItem<T> {
    fun same(other: T) : Boolean
}