package com.example.tagplayer.tagsettings.presentation

import com.example.tagplayer.main.presentation.GenericAdapterWithInit
import com.example.tagplayer.main.presentation.ItemUiTypeWithInit

class TagsAdapter(
    menuOptions: List<Pair<Int, MenuAction>>
) : GenericAdapterWithInit.ItemUiWithInitAdapter(
    menuOptions,
    listOf(ItemUiTypeWithInit.TagSettingsType)
)