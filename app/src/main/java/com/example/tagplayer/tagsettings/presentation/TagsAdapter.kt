package com.example.tagplayer.tagsettings.presentation

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericAdapterMenu
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuType

class TagsAdapter(
    menuOptions: List<Pair<Int, MenuAction>>
) : GenericAdapterMenu<TagSettingsUi>(
    GenericDiffUtil(),
    menuOptions,
    listOf(ItemUiMenuType.TagSettingsMenuType)
)