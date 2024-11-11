package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.CustomObserver

interface TagFiltersObserver : CustomObserver<TagFiltersResponse> {
    object Empty : TagFiltersObserver {
        override fun update(data: TagFiltersResponse) = Unit
    }
}