package com.example.tagplayer.history.presentation

import androidx.recyclerview.widget.DiffUtil

object HistoryDiffUtil : DiffUtil.ItemCallback<HistoryUi>() {
    override fun areItemsTheSame(oldItem: HistoryUi, newItem: HistoryUi) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: HistoryUi, newItem: HistoryUi) =
        oldItem.same(newItem)
}