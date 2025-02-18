package com.example.tagplayer.filter.presentation

import com.example.tagplayer.core.presentation.observable.CustomObserver

interface FilterObserver : CustomObserver<FilterState> {
    object Empty : FilterObserver {
        override fun update(data: FilterState) = Unit
    }
}