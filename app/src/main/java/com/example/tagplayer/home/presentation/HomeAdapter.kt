package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.presentation.generic_adapter.adapter.GenericAdapterMenuListener
import com.example.tagplayer.core.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction
import com.example.tagplayer.core.presentation.custom_views.interfaces.UpdateList

class HomeAdapter(
    menuOptions: List<Pair<Int, MenuAction>>,
    listener: (Long) -> Unit,
) : GenericAdapterMenuListener<SongUi, Long>(
    GenericDiffUtil(),
    menuOptions,
    listener,
    listOf(ItemUIMenuListenerType.HomeSongMenuType)
), UpdateList<SongUi> {

    override fun update(list: List<SongUi>, block: () -> Unit) =
        submitList(list) { block.invoke() }
}