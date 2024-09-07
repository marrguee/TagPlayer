package com.example.tagplayer.all.presentation

import com.example.tagplayer.main.presentation.GenericAdapter
import com.example.tagplayer.main.presentation.ItemUiType

class RecentlyRecyclerAdapter(
    listener: (Long) -> Unit,
) : GenericAdapter.ItemUiAdapter(
    listener,
    listOf(ItemUiType.LibraryType)
)