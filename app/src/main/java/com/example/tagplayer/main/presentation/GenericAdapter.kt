package com.example.tagplayer.main.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

@Suppress("UNCHECKED_CAST")
abstract class GenericAdapter<T : ItemUi>(
    diffUtil: GenericDiffUtil<T>,
    private val listener: (Long) -> Unit,
    private val typeList: List<ItemUiType>
) : ListAdapter<T, GenericViewHolder<T>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        typeList[viewType].viewHolder(parent) as GenericViewHolder<T>

    override fun getItemViewType(position: Int) =
        typeList.indexOf(currentList[position].type())

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        holder.bind(currentList[position], listener)
    }

    open class ItemUiAdapter(
        listener: (Long) -> Unit,
        typeList: List<ItemUiType>
    ) : GenericAdapter<ItemUi>(
        ItemUiDiffUtil,
        listener,
        typeList
    )
}

//abstract class GenericListenerAdapter<T : ItemUi>(
//    diffUtil: GenericDiffUtil<T>,
//    private val listener: (Long) -> Unit,
//    private val typeList: List<ItemUiType>
//) : ListAdapter<T, GenericListenerViewHolder<T>>(diffUtil) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//        typeList[viewType].viewHolder(parent) as GenericListenerViewHolder<T>
//
//    override fun getItemViewType(position: Int) =
//        typeList.indexOf(currentList[position].type())
//
//    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
//        holder.bind(currentList[position], listener)
//    }
//
//}
//
//interface GenericListenerViewHolder<T : ItemUi> {
//    fun bind(item: T)
//    fun bind(item: T, listener: (Long) -> Unit)
//}