package com.example.tagplayer.playback.presentation

import com.example.tagplayer.main.presentation.generic_adapter.adapter.GenericAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiType

class TagsPlaybackAdapter :
    GenericAdapter<TagPlaybackUi>(GenericDiffUtil(), listOf(ItemUiType.TagPlaybackType))