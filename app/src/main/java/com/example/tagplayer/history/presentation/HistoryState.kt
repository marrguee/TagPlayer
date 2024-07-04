package com.example.tagplayer.history.presentation

import com.example.tagplayer.main.presentation.ItemUi

interface HistoryState {
    fun dispatch(adapter: HistoryRecyclerAdapter)
    class HistoryUpdated(private val list: List<ItemUi>) : HistoryState {
        override fun dispatch(adapter: HistoryRecyclerAdapter) {
            adapter.submitList(list)
        }
    }

    class Error(private val cause: String) : HistoryState {
        override fun dispatch(adapter: HistoryRecyclerAdapter) = Unit
    }
}