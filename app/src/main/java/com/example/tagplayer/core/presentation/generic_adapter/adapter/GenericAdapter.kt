package com.example.tagplayer.core.presentation.generic_adapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tagplayer.core.presentation.generic_adapter.holder.GenericViewHolder

@Suppress("UNCHECKED_CAST")
abstract class GenericAdapter<T : com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.ItemUi>(
    diffUtil: com.example.tagplayer.core.presentation.generic_adapter.diffutils.GenericDiffUtil<T>,
    private val typeList: List<com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiType>
) : ListAdapter<T, GenericViewHolder<T>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        typeList[viewType].viewHolder(parent) as GenericViewHolder<T>

    override fun getItemViewType(position: Int) =
        typeList.indexOf(currentList[position].type())

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        holder.bind(currentList[position])
    }

    open class ItemUiAdapter(
        typeList: List<com.example.tagplayer.core.presentation.generic_adapter.types.ItemUiType>
    ) : com.example.tagplayer.core.presentation.generic_adapter.adapter.GenericAdapter<com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.ItemUi>(
        com.example.tagplayer.core.presentation.generic_adapter.diffutils.GenericDiffUtil(),
        typeList
    )
}