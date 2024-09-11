package com.example.tagplayer.core.domain

interface CompareContent {
    fun compare(otherId: Long) : Boolean
    fun compare(otherDate: String) : Boolean
    fun compare(otherBoolean: Boolean) : Boolean
}