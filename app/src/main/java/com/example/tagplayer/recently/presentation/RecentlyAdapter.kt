package com.example.tagplayer.recently.presentation

import com.example.tagplayer.core.presentation.generic_adapter.adapter.GenericAdapterMenuListener
import com.example.tagplayer.core.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction

class RecentlyAdapter(
    menuOptions: List<Pair<Int, MenuAction>>,
    listener: (Long) -> Unit,
) : GenericAdapterMenuListener<RecentlyUi, Long>(
    GenericDiffUtil(),
    menuOptions,
    listener,
    listOf(ItemUIMenuListenerType.RecentlyMenuType)
)