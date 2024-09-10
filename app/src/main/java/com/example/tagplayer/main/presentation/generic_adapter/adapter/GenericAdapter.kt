package com.example.tagplayer.main.presentation.generic_adapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tagplayer.main.presentation.ItemUi
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericViewHolder
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiType

@Suppress("UNCHECKED_CAST")
abstract class GenericAdapter<T : ItemUi>(
    diffUtil: GenericDiffUtil<T>,
    private val typeList: List<ItemUiType>
) : ListAdapter<T, GenericViewHolder<T>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        typeList[viewType].viewHolder(parent) as GenericViewHolder<T>

    override fun getItemViewType(position: Int) =
        typeList.indexOf(currentList[position].type())

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        holder.bind(currentList[position])
    }

    open class ItemUiAdapter(
        typeList: List<ItemUiType>
    ) : GenericAdapter<ItemUi>(
        GenericDiffUtil(),
        typeList
    )
}