package com.example.tagplayer.search.presentation

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericListenerAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType

class SongSearchListenerAdapter(
    listener: (Long) -> Unit
) : GenericListenerAdapter<SongSearchUi, Long>(
    GenericDiffUtil(),
    listener,
    listOf(ItemUiListenerType.SongListenerType)
)