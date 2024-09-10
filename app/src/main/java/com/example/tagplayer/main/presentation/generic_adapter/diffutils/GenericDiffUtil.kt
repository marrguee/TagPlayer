package com.example.tagplayer.main.presentation.generic_adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.tagplayer.main.presentation.ItemUiBase

class GenericDiffUtil<T : ItemUiBase> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem.same(newItem)
}