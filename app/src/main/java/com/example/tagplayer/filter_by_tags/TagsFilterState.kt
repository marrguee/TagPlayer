package com.example.tagplayer.filter_by_tags

import com.example.tagplayer.core.domain.HandleUiStateUpdates

interface TagsFilterState {
    fun dispatch(adapter: FilterTagsAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = viewModel.clearObserver()

    class SelectedTagsChanged(
        private val list: List<TagFilterUi>
    ) : TagsFilterState {
        override fun dispatch(adapter: FilterTagsAdapter) {
            adapter.submitList(list)
        }
    }

    object Empty : TagsFilterState {
        override fun dispatch(adapter: FilterTagsAdapter) = Unit
    }
}