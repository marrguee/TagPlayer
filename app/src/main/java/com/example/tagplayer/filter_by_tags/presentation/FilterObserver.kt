package com.example.tagplayer.filter_by_tags.presentation

import com.example.tagplayer.core.CustomObserver

interface FilterObserver : CustomObserver<FilterScreenState> {
    object Empty : FilterObserver {
        override fun update(data: FilterScreenState) = Unit
    }
}