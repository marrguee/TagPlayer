package com.example.tagplayer.search.presentation

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericAdapter
import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericListenerAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiType

class SearchListenerAdapter(
    listener: (Long) -> Unit
) : GenericAdapter<SearchUi>(
    GenericDiffUtil(),
    listOf(ItemUiType.TagType)
)