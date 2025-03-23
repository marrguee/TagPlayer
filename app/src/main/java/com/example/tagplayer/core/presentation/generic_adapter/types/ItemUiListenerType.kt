package com.example.tagplayer.core.presentation.generic_adapter.types

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.generic_adapter.holder.GenericListenerViewHolder
import com.example.tagplayer.core.presentation.generic_adapter.holder.GenericViewHolder

interface ItemUiType {
    fun viewHolder(parent: ViewGroup): GenericViewHolder<*>

    object TagType : ItemUiType {
        override fun viewHolder(parent: ViewGroup): GenericViewHolder<*> =
            GenericViewHolder.TagHolderListener(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tag, parent, false)
            )
    }

    object TagPlaybackType : ItemUiType {
        override fun viewHolder(parent: ViewGroup): GenericViewHolder<*> =
            GenericViewHolder.TagHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tag_playback, parent, false)
            )
    }

}

interface ItemUiListenerType {
    fun viewHolder(parent: ViewGroup): GenericListenerViewHolder<*, *>

    object TagFilterListenerType : ItemUiListenerType {
        override fun viewHolder(parent: ViewGroup) =
            GenericListenerViewHolder.TagFilterHolderListener(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tag, parent, false)
            )
    }
}

