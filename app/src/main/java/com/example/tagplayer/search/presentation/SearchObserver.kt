package com.example.tagplayer.search.presentation

import com.example.tagplayer.core.presentation.observable.CustomObserver

interface SearchObserver : CustomObserver<SearchState> {
    object Empty : SearchObserver {
        override fun update(data: SearchState) = Unit
    }
}