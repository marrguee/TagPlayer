package com.example.tagplayer.tagsettings.presentation

import com.example.tagplayer.core.domain.HandleUiStateUpdates

interface TagSettingsState {
    fun dispatch(adapter: TagsAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = viewModel.clearObserver()

    class UpdateTagList(private val list: List<TagSettingsUi>) : TagSettingsState {
        override fun dispatch(adapter: TagsAdapter) {
            adapter.submitList(list)
        }
    }

    class Error(private val message: String) : TagSettingsState {
        override fun dispatch(adapter: TagsAdapter) = Unit
    }

    object Empty : TagSettingsState {
        override fun dispatch(adapter: TagsAdapter) = Unit
        override fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = Unit
    }
}