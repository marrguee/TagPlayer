package com.example.tagplayer.filter_by_tags.presentation

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericListenerAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType

class FilterAdapter(
    listener: (Long) -> Unit
) : GenericListenerAdapter<FilterUi, Long>(
    GenericDiffUtil(),
    listener,
    listOf(ItemUiListenerType.TagFilterListenerType)
)