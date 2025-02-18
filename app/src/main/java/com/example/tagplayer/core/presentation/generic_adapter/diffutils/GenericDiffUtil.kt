package com.example.tagplayer.core.presentation.generic_adapter.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.ItemUiBase

class GenericDiffUtil<T : ItemUiBase> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem.same(newItem)
}