package com.example.tagplayer.recently.presentation

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericAdapterMenuListener
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericMenuListenerViewHolder
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.tag_settings.presentation.MenuAction

class RecentlyListenerAdapter(
    menuOptions: List<Pair<Int, MenuAction>>,
    listener: (Long) -> Unit,
) : GenericAdapterMenuListener<RecentlyUi, Long>(
    GenericDiffUtil(),
    menuOptions,
    listener,
    listOf(ItemUIMenuListenerType.RecentlyMenuType)
)