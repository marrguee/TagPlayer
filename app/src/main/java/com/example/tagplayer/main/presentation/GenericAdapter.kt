package com.example.tagplayer.main.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tagplayer.tagsettings.presentation.MenuAction

@Suppress("UNCHECKED_CAST")
abstract class GenericAdapter<T : ItemUiSimple>(
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
    ) : GenericAdapter<ItemUiSimple>(
        ItemUiSimpleDiffUtil,
        listener,
        typeList
    )
}

abstract class GenericAdapterWithInit<T : ItemUiMenu>(
    diffUtil: GenericDiffUtil<T>,
    private val menuOptions: List<Pair<Int, MenuAction>>,
    private val typeList: List<ItemUiTypeWithInit>
) : ListAdapter<T, GenericMenuViewHolder<T>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        typeList[viewType].viewHolder(parent) as GenericMenuViewHolder<T>

    override fun getItemViewType(position: Int) =
        typeList.indexOf(currentList[position].type())

    override fun onBindViewHolder(holder: GenericMenuViewHolder<T>, position: Int) {
        holder.bind(currentList[position], menuOptions)
    }

    open class ItemUiWithInitAdapter(
        menuOptions: List<Pair<Int, MenuAction>>,
        typeList: List<ItemUiTypeWithInit>
    ) : GenericAdapterWithInit<ItemUiMenu>(
        ItemUiMenuDiffUtil,
        menuOptions,
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