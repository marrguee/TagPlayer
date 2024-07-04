package com.example.tagplayer.all.presentation

import com.example.tagplayer.main.presentation.GenericAdapter
import com.example.tagplayer.main.presentation.ItemUiType

class AllRecyclerAdapter(
    listener: (Long) -> Unit,
) : GenericAdapter.ItemUiAdapter(
    listener,
    listOf(ItemUiType.SongType)
)