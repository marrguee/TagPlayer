package com.example.tagplayer.tagsettings.presentation

import com.example.tagplayer.core.CustomObserver

interface TagSettingsObserver : CustomObserver<TagSettingsState> {
    object Empty : TagSettingsObserver {
        override fun update(data: TagSettingsState) = Unit
    }
}