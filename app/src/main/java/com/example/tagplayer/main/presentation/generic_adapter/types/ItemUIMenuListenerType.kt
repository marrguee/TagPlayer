package com.example.tagplayer.main.presentation.generic_adapter.types

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tagplayer.R
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericMenuListenerViewHolder

interface ItemUIMenuListenerType {
    fun viewHolder(parent: ViewGroup): GenericMenuListenerViewHolder<*, *>

    object HomeSongMenuType : ItemUIMenuListenerType {
        override fun viewHolder(parent: ViewGroup) =
            GenericMenuListenerViewHolder.HomeSongHolderMenu(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false),
            )
    }
}