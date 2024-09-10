package com.example.tagplayer.main.presentation.generic_adapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericMenuListenerViewHolder
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUIMenuListenerType
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuListener
import com.example.tagplayer.tagsettings.presentation.MenuAction

@Suppress("UNCHECKED_CAST")
abstract class GenericAdapterMenuListener<T : ItemUiMenuListener, L>(
    diffUtil: GenericDiffUtil<T>,
    private val menuOptions: List<Pair<Int, MenuAction>>,
    private val listener: (L) -> Unit,
    private val typeList: List<ItemUIMenuListenerType>
) : ListAdapter<T, GenericMenuListenerViewHolder<T, L>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        typeList[viewType].viewHolder(parent) as GenericMenuListenerViewHolder<T, L>

    override fun getItemViewType(position: Int) =
        typeList.indexOf(currentList[position].type())

    override fun onBindViewHolder(holder: GenericMenuListenerViewHolder<T, L>, position: Int) {
        holder.bind(currentList[position], menuOptions, listener)
    }
}