package com.example.tagplayer.recently.presentation

import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.core.domain.HandleUiStateUpdates

interface RecentlyState {
    fun dispatch(recyclerView: RecyclerView)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = viewModel.clearObserver()

    class RecentlyUpdated(private val list: List<RecentlyUi>) : RecentlyState {
        override fun dispatch(recyclerView: RecyclerView) {
            (recyclerView.adapter as RecentlyListenerAdapter).submitList(list)
        }
    }

    class Error(private val cause: String) : RecentlyState {
        override fun dispatch(recyclerView: RecyclerView) = Unit
    }

    object Empty : RecentlyState {
        override fun dispatch(recyclerView: RecyclerView) = Unit
        override fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = Unit
    }
}