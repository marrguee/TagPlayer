package com.example.tagplayer.recently.presentation

import com.example.tagplayer.core.CustomObserver

interface RecentlyObserver : CustomObserver<RecentlyState> {
    object Empty : RecentlyObserver {
        override fun update(data: RecentlyState) = Unit
    }
}