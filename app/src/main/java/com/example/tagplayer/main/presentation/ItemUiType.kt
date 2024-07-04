package com.example.tagplayer.main.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tagplayer.R

interface ItemUiType {
    fun viewHolder(parent: ViewGroup) : GenericViewHolder<*>

    object SongType : ItemUiType {
        override fun viewHolder(parent: ViewGroup) =
            GenericViewHolder.SongHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.track_item, parent, false)
            )
    }

    object DateType : ItemUiType {
        override fun viewHolder(parent: ViewGroup)=
            GenericViewHolder.DateHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.date_holder_item, parent, false)
            )
    }
}