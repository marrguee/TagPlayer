package com.example.tagplayer.tagsettings.presentation

interface TagSettingsState {
    fun dispatch()

    class UpdateTagList(private val list: List<TagUi>) : TagSettingsState {
        override fun dispatch() = Unit
    }

    class Error(private val message: String) : TagSettingsState {
        override fun dispatch() = Unit
    }
}