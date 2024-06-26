package com.example.tagplayer.history.presentation

interface HistoryState {
    fun dispatch(adapter: HistoryRecyclerAdapter)
    class HistoryUpdated(private val list: List<HistoryUi>) : HistoryState {
        override fun dispatch(adapter: HistoryRecyclerAdapter) {
            adapter.submitList(list)
        }
    }

    class Error(private val cause: String) : HistoryState {
        override fun dispatch(adapter: HistoryRecyclerAdapter) = Unit
    }
}