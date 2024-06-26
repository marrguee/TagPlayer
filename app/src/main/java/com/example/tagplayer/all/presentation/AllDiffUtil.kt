package com.example.tagplayer.all.presentation

import androidx.recyclerview.widget.DiffUtil

object AllDiffUtil : DiffUtil.ItemCallback<SongUi>() {
    override fun areItemsTheSame(oldItem: SongUi, newItem: SongUi) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: SongUi, newItem: SongUi) =
        oldItem.same(newItem)

}