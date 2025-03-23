package com.example.tagplayer.filter.presentation

import com.example.tagplayer.core.presentation.generic_adapter.adapter.GenericListenerAdapter
import com.example.tagplayer.core.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiListenerType

class FilterAdapter(
    listener: (Pair<Long, Boolean>) -> Unit,
) : GenericListenerAdapter<FilterUi, Pair<Long, Boolean>>(
    GenericDiffUtil(),
    listener,
    listOf(ItemUiListenerType.TagFilterListenerType)
)