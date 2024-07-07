package com.example.tagplayer.main.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.history.presentation.DateUi

abstract class GenericViewHolder<T : ItemUi>(root: View) : RecyclerView.ViewHolder(root) {
    abstract fun bind(item: T, listener: (Long) -> Unit)

    abstract class GenericListenerViewHolder<T : ItemUi>(root: View) : GenericViewHolder<T>(root)

    class SongHolder(root: View) : GenericViewHolder<SongUi>(root) {
        private val titleTextView: CustomTextView = itemView.findViewById(R.id.titleTextView)
        private val durationTextView: CustomTextView = itemView.findViewById(R.id.durationTextView)
        override fun bind(item: SongUi, listener: (Long) -> Unit) {
            item.bind(titleTextView, durationTextView)
            itemView.setOnClickListener { item.tap(listener) }
        }
    }

    class DateHolder(root: View) : GenericViewHolder<DateUi>(root) {
        private val dateTextView: CustomTextView = itemView.findViewById(R.id.dateTextView)

        override fun bind(item: DateUi, listener: (Long) -> Unit) {
            item.bind(dateTextView)
        }
    }
}