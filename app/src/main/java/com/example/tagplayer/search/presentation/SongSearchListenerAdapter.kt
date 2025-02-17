package com.example.tagplayer.search.presentation

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericAdapterMenuListener
import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericListenerAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType
import com.example.tagplayer.tag_settings.presentation.MenuAction

class SongSearchListenerAdapter(
    menuOptions: List<Pair<Int, MenuAction>>,
    listener: (Long) -> Unit
) : GenericAdapterMenuListener<SongSearchUi, Long>(
    GenericDiffUtil(),
    menuOptions,
    listener,
    listOf(ItemUIMenuListenerType.SearchMenuType)
)