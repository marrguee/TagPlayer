package com.example.tagplayer.search.presentation

import com.example.tagplayer.core.presentation.generic_adapter.adapter.GenericAdapterMenuListener
import com.example.tagplayer.core.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction

class SearchAdapter(
    menuOptions: List<Pair<Int, MenuAction>>,
    listener: (Long) -> Unit
) : GenericAdapterMenuListener<SearchUi, Long>(
    GenericDiffUtil(),
    menuOptions,
    listener,
    listOf(ItemUIMenuListenerType.SearchMenuType)
)