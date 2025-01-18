package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.CustomObserver

interface TagFiltersObserver : CustomObserver<TagFiltersState> {
    object Empty : TagFiltersObserver {
        override fun update(data: TagFiltersState) = Unit
    }
}