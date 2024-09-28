package com.example.tagplayer.main.presentation.generic_adapter.types

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tagplayer.R
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericListenerViewHolder
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericViewHolder

interface ItemUiType {
    fun viewHolder(parent: ViewGroup): GenericViewHolder<*>

    object TagType : ItemUiType {
        override fun viewHolder(parent: ViewGroup): GenericViewHolder<*> =
            GenericViewHolder.TagHolderListener(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tag, parent, false)
            )
    }

//    object SearchType : ItemUiType {
//        override fun viewHolder(parent: ViewGroup): GenericViewHolder<*> =
//            GenericViewHolder.SearchHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.item_search, parent, false)
//            )
//    }
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

    object RecentlyListenerType : ItemUiListenerType {
        override fun viewHolder(parent: ViewGroup) =
            GenericListenerViewHolder.RecentlyHolderListener(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false)
            )
    }

    object RecentlyDateListenerType : ItemUiListenerType {
        override fun viewHolder(parent: ViewGroup) =
            GenericListenerViewHolder.RecentlyDateHolderListener(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_date, parent, false)
            )
    }

    object SongListenerType : ItemUiListenerType {
        override fun viewHolder(parent: ViewGroup): GenericListenerViewHolder<*, *> =
            GenericListenerViewHolder.SongHolderListener(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false)
            )
    }
}

