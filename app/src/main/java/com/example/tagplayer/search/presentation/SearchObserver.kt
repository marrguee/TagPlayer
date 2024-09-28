package com.example.tagplayer.search.presentation

import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.search.domain.SearchState

interface SearchObserver : CustomObserver<SearchState> {
    object Empty : SearchObserver {
        override fun update(data: SearchState) = Unit
    }
}