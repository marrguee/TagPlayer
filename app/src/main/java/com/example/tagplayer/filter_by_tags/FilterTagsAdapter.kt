package com.example.tagplayer.filter_by_tags

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericListenerAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType

class FilterTagsAdapter(
    listener: (Long) -> Unit
) : GenericListenerAdapter<TagFilterUi, Long>(
    GenericDiffUtil(),
    listener,
    listOf(ItemUiListenerType.TagFilterListenerType)
)