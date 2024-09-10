package com.example.tagplayer.recently.presentation

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericListenerAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType

class RecentlyListenerAdapter(
    listener: (Long) -> Unit,
) : GenericListenerAdapter<RecentlyUi, Long>(
    GenericDiffUtil(),
    listener,
    listOf(ItemUiListenerType.RecentlyListenerType, ItemUiListenerType.RecentlyDateListenerType)
)