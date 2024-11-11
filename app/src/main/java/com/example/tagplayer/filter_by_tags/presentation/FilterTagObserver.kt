package com.example.tagplayer.filter_by_tags.presentation

import com.example.tagplayer.core.CustomObserver

interface FilterTagObserver : CustomObserver<TagsFilterState> {
    object Empty : FilterTagObserver {
        override fun update(data: TagsFilterState) = Unit
    }
}