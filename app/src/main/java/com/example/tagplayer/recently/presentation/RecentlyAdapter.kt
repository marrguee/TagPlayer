package com.example.tagplayer.recently.presentation

import com.example.tagplayer.main.presentation.GenericAdapter
import com.example.tagplayer.main.presentation.ItemUiType

class RecentlyAdapter(
    listener: (Long) -> Unit,
) : GenericAdapter.ItemUiAdapter(
    listener,
    listOf(ItemUiType.RecentlyType, ItemUiType.RecentlyDateType)
)