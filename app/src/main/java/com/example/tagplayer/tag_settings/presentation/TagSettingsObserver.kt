package com.example.tagplayer.tag_settings.presentation

import com.example.tagplayer.core.CustomObserver

interface TagSettingsObserver : CustomObserver<TagSettingsState> {
    object Empty : TagSettingsObserver {
        override fun update(data: TagSettingsState) = Unit
    }
}