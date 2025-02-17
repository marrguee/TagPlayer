package com.example.tagplayer.main.presentation.generic_adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.core.CustomTextView
import com.example.tagplayer.filter_by_tags.presentation.FilterUi
import com.example.tagplayer.main.presentation.ItemUiListener

abstract class GenericListenerViewHolder<T : ItemUiListener, L>(root: View) :
    RecyclerView.ViewHolder(root) {
    abstract fun bind(item: T, listener: (L) -> Unit)

    class TagFilterHolderListener(root: View) : GenericListenerViewHolder<FilterUi, Long>(root) {
        private val textView: CustomTextView = itemView.findViewById(R.id.tagTextView)
        override fun bind(item: FilterUi, listener: (Long) -> Unit) {
            item.bind(textView)
            textView.setOnClickListener {
                item.tap(listener)
            }
        }
    }
}