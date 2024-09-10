package com.example.tagplayer.main.presentation.generic_adapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tagplayer.main.presentation.ItemUiListener
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericListenerViewHolder
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType


@Suppress("UNCHECKED_CAST")
abstract class GenericListenerAdapter<T : ItemUiListener, L>(
    diffUtil: GenericDiffUtil<T>,
    private val listener: (L) -> Unit,
    private val typeList: List<ItemUiListenerType>
) : ListAdapter<T, GenericListenerViewHolder<T, L>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        typeList[viewType].viewHolder(parent) as GenericListenerViewHolder<T, L>

    override fun getItemViewType(position: Int) =
        typeList.indexOf(currentList[position].type())

    override fun onBindViewHolder(holder: GenericListenerViewHolder<T, L>, position: Int) {
        holder.bind(currentList[position], listener)
    }
}

