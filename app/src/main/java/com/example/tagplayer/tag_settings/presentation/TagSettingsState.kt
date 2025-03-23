package com.example.tagplayer.tag_settings.presentation

import android.content.Context
import android.widget.Toast
import com.example.tagplayer.core.domain.HandleUiStateUpdates

interface TagSettingsState {
    fun dispatch(context: Context, adapter: TagsAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

    class UpdateTags(private val list: List<TagSettingsUi>) : TagSettingsState {
        override fun dispatch(context: Context, adapter: TagsAdapter) = adapter.submitList(list)
    }

    class Error(private val error: String) : TagSettingsState {
        override fun dispatch(context: Context, adapter: TagsAdapter) =
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()
    }

    object Empty : TagSettingsState {
        override fun dispatch(context: Context, adapter: TagsAdapter) = Unit
    }
}