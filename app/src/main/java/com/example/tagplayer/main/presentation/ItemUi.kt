package com.example.tagplayer.main.presentation

import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.CompareItem

interface ItemUi : CompareItem, CompareContent {
    fun type() : ItemUiType

    fun bind(vararg views: MyView)
}