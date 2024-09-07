package com.example.tagplayer.main.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tagplayer.R

interface ItemUiType {
    fun viewHolder(parent: ViewGroup, registerWithContextMenu: (View) -> Unit = {}): GenericViewHolder<*>

//    object TagType : ItemUiType {
//        override fun viewHolder(parent: ViewGroup, registerWithContextMenu: (View) -> Unit) =
//            GenericViewHolder.TagHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.item_tag, parent, false)
//            )
//    }

    object LibraryType : ItemUiType {
        override fun viewHolder(parent: ViewGroup, registerWithContextMenu: (View) -> Unit) =
            GenericViewHolder.LibraryHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false)
            )
    }

    object RecentlyType : ItemUiType {
        override fun viewHolder(parent: ViewGroup, registerWithContextMenu: (View) -> Unit) =
            GenericViewHolder.RecentlyHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false)
            )
    }

    object RecentlyDateType : ItemUiType {
        override fun viewHolder(parent: ViewGroup, registerWithContextMenu: (View) -> Unit) =
            GenericViewHolder.RecentlyDateHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_date, parent, false)
            )
    }
}

interface ItemUiTypeWithInit {
    fun viewHolder(parent: ViewGroup): GenericMenuViewHolder<*>

    object TagSettingsType : ItemUiTypeWithInit {
        override fun viewHolder(parent: ViewGroup) =
            GenericMenuViewHolder.TagSettingsHolderMenu(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tag, parent, false),
            )
    }
}