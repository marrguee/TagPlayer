package com.example.tagplayer.recently.presentation

import androidx.recyclerview.widget.RecyclerView

interface RecentlyState {
    fun dispatch(recyclerView: RecyclerView)

    class RecentlyUpdated(private val list: List<RecentlyUi>) : RecentlyState {
        override fun dispatch(recyclerView: RecyclerView) {
            (recyclerView.adapter as RecentlyListenerAdapter).submitList(list)
        }
    }

    class Error(private val cause: String) : RecentlyState {
        override fun dispatch(recyclerView: RecyclerView) = Unit
    }
}