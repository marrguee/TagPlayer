package com.example.tagplayer.core.presentation.generic_adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.custom_views.CustomTextView
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.ItemUiListener
import com.example.tagplayer.filter.presentation.FilterUi

abstract class GenericListenerViewHolder<T : ItemUiListener, L>(root: View) :
    RecyclerView.ViewHolder(root) {
    abstract fun bind(item: T, listener: (L) -> Unit)

    class TagFilterHolderListener(root: View) :
        GenericListenerViewHolder<FilterUi, Pair<Long, Boolean>>(root) {
        private val textView: CustomTextView = itemView.findViewById(R.id.tagTextView)
        override fun bind(item: FilterUi, listener: (Pair<Long, Boolean>) -> Unit) {
            item.bind(textView)
            textView.setOnClickListener {
                item.tap(listener)
            }
        }
    }
}