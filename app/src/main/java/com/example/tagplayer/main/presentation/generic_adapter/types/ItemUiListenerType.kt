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
}

interface ItemUiListenerType {
    fun viewHolder(parent: ViewGroup): GenericListenerViewHolder<*, *>

//    object TagSimpleListenerType : ItemUiListenerType {
//        override fun viewHolder(parent: ViewGroup) =
//            GenericListenerViewHolder.TagSimpleHolderListener(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.item_tag, parent, false)
//            )
//    }
//
//    object TagSimpleSelectedListenerType : ItemUiListenerType {
//        override fun viewHolder(parent: ViewGroup) =
//            GenericListenerViewHolder.TagSimpleHolderListener(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.item_tag_selected, parent, false)
//            )
//    }

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
}
