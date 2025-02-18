package com.example.tagplayer.recently.presentation

import android.content.Context
import android.os.Parcelable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import kotlinx.parcelize.Parcelize

interface RecentlyState {
    fun dispatch(context: Context, adapter: RecentlyAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

    data class RecentlyUpdated(private val list: List<RecentlyUi>) : RecentlyState {
        override fun dispatch(context: Context, adapter: RecentlyAdapter) =
            adapter.submitList(list)

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }

    data class Error(private val error: String) : RecentlyState {
        override fun dispatch(context: Context, adapter: RecentlyAdapter) =
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) =
            viewModel.clear()
    }

    object Empty : RecentlyState {
        override fun dispatch(context: Context, adapter: RecentlyAdapter) = Unit
        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }
}