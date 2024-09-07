package com.example.tagplayer.main.presentation

import androidx.recyclerview.widget.DiffUtil

abstract class GenericDiffUtil<T : ItemUiBase> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem.same(newItem)
}