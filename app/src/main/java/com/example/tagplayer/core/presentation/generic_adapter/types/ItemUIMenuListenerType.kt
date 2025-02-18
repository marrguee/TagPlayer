package com.example.tagplayer.core.presentation.generic_adapter.types

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.generic_adapter.holder.GenericMenuListenerViewHolder

interface ItemUIMenuListenerType {
    fun viewHolder(parent: ViewGroup): com.example.tagplayer.core.presentation.generic_adapter.holder.GenericMenuListenerViewHolder<*, *>

    object HomeSongMenuType :
        com.example.tagplayer.core.presentation.generic_adapter.types.ItemUIMenuListenerType {
        override fun viewHolder(parent: ViewGroup) =
            com.example.tagplayer.core.presentation.generic_adapter.holder.GenericMenuListenerViewHolder.HomeSongHolderMenu(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false),
            )
    }

    object SearchMenuType :
        com.example.tagplayer.core.presentation.generic_adapter.types.ItemUIMenuListenerType {
        override fun viewHolder(parent: ViewGroup) =
            com.example.tagplayer.core.presentation.generic_adapter.holder.GenericMenuListenerViewHolder.SearchHolderMenu(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false),
            )
    }

    object RecentlyMenuType :
        com.example.tagplayer.core.presentation.generic_adapter.types.ItemUIMenuListenerType {
        override fun viewHolder(parent: ViewGroup) =
            com.example.tagplayer.core.presentation.generic_adapter.holder.GenericMenuListenerViewHolder.RecentlyHolderMenu(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false),
            )
    }
}