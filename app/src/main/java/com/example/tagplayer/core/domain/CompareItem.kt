package com.example.tagplayer.core.domain

interface CompareItem {
    fun same(other: CompareContent) : Boolean
}