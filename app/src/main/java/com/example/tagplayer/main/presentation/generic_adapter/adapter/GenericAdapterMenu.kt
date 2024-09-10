package com.example.tagplayer.main.presentation.generic_adapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tagplayer.main.presentation.generic_adapter.diffutils.GenericDiffUtil
import com.example.tagplayer.main.presentation.generic_adapter.holder.GenericMenuViewHolder
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuType
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiMenuWithoutListener
import com.example.tagplayer.tagsettings.presentation.MenuAction

@Suppress("UNCHECKED_CAST")
abstract class GenericAdapterMenu<T : ItemUiMenuWithoutListener>(
    diffUtil: GenericDiffUtil<T>,
    private val menuOptions: List<Pair<Int, MenuAction>>,
    private val typeList: List<ItemUiMenuType>
) : ListAdapter<T, GenericMenuViewHolder<T>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        typeList[viewType].viewHolder(parent) as GenericMenuViewHolder<T>

    override fun getItemViewType(position: Int) =
        typeList.indexOf(currentList[position].type())

    override fun onBindViewHolder(holder: GenericMenuViewHolder<T>, position: Int) {
        holder.bind(currentList[position], menuOptions)
    }
}