package com.example.tagplayer.core.presentation.generic_adapter.types

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.generic_adapter.holder.GenericMenuViewHolder

interface ItemUiMenuType {
    fun viewHolder(parent: ViewGroup): com.example.tagplayer.core.presentation.generic_adapter.holder.GenericMenuViewHolder<*>

    object TagSettingsMenuType :
        com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiMenuType {
        override fun viewHolder(parent: ViewGroup) =
            com.example.tagplayer.core.presentation.generic_adapter.holder.GenericMenuViewHolder.TagSettingsHolderMenu(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tag, parent, false),
            )
    }
}