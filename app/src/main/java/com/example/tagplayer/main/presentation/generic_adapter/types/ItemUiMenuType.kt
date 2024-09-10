package com.example.tagplayer.main.presentation.generic_adapter.types

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tagplayer.R
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericMenuViewHolder

interface ItemUiMenuType {
    fun viewHolder(parent: ViewGroup): GenericMenuViewHolder<*>

    object TagSettingsMenuType : ItemUiMenuType {
        override fun viewHolder(parent: ViewGroup) =
            GenericMenuViewHolder.TagSettingsHolderMenu(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tag, parent, false),
            )
    }
}