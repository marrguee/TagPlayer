package com.example.tagplayer.filter_by_tags

interface TagsFilterState {
    fun dispatch(adapter: FilterTagsAdapter)

    class SelectedTagsChanged(
        private val list: List<TagFilterUi>
    ) : TagsFilterState {
        override fun dispatch(adapter: FilterTagsAdapter) {
            adapter.submitList(list)
        }
    }
}