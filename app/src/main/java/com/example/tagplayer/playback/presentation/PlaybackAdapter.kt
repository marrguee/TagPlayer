package com.example.tagplayer.playback.presentation

import com.example.tagplayer.core.presentation.generic_adapter.adapter.GenericAdapter
import com.example.tagplayer.core.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiType

class PlaybackAdapter : GenericAdapter<TagPlaybackUi>(
    GenericDiffUtil(),
    listOf(ItemUiType.TagPlaybackType)
)