package com.example.tagplayer.main.presentation.generic_adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.edit_song_tag.TagUi
import com.example.tagplayer.main.presentation.CustomTextView
import com.example.tagplayer.main.presentation.ItemUiListener
import com.example.tagplayer.recently.presentation.RecentlyUi.DateUi
import com.example.tagplayer.recently.presentation.RecentlyUi.RecentlySongUi

abstract class GenericListenerViewHolder<T : ItemUiListener, L>(root: View) : RecyclerView.ViewHolder(root) {
    abstract fun bind(item: T, listener: (L) -> Unit)

    class RecentlyHolderListener(root: View) : GenericListenerViewHolder<RecentlySongUi, Long>(root) {
        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)
        override fun bind(item: RecentlySongUi, listener: (Long) -> Unit) {
            item.bind(titleTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }
    }

    class RecentlyDateHolderListener(root: View) : GenericListenerViewHolder<DateUi, Any>(root) {
        private val dateTextView: CustomTextView = itemView.findViewById(R.id.dateTextView)

        override fun bind(item: DateUi, listener: (Any) -> Unit) {
            item.bind(dateTextView)
        }
    }
}

