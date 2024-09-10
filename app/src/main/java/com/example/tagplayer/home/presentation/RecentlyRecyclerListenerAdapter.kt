package com.example.tagplayer.home.presentation

import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericAdapterMenuListener
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.tagsettings.presentation.MenuAction

class RecentlyRecyclerListenerAdapter(
    menuOptions: List<Pair<Int, MenuAction>>,
    listener: (Long) -> Unit,
) : GenericAdapterMenuListener<SongUi, Long>(
    GenericDiffUtil(),
    menuOptions,
    listener,
    listOf(ItemUIMenuListenerType.HomeSongMenuType)
)