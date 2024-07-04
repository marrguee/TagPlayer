package com.example.tagplayer.history.presentation

import com.example.tagplayer.main.presentation.GenericAdapter
import com.example.tagplayer.main.presentation.ItemUiType

class HistoryRecyclerAdapter(
    listener: (Long) -> Unit,
) : GenericAdapter.ItemUiAdapter(
    listener,
    listOf(ItemUiType.SongType, ItemUiType.DateType)
)