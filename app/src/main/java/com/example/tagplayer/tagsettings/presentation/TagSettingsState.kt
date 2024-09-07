package com.example.tagplayer.tagsettings.presentation

interface TagSettingsState {
    fun dispatch(adapter: TagsAdapter)

    class UpdateTagList(private val list: List<TagSettingsUi>) : TagSettingsState {
        override fun dispatch(adapter: TagsAdapter) {
            adapter.submitList(list)
        }
    }

    class Error(private val message: String) : TagSettingsState {
        override fun dispatch(adapter: TagsAdapter) = Unit
    }
}