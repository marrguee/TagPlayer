package com.example.tagplayer.home.presentation

import com.example.tagplayer.home.domain.SortType

interface SortSongs {
    fun sort(type: SortType.Map)
}