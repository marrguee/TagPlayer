package com.example.tagplayer.filter.presentation

import android.content.Context
import android.os.Parcelable
import android.widget.Toast
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import kotlinx.parcelize.Parcelize

interface FilterState : Parcelable {
    fun dispatch(context: Context, adapter: FilterAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

    @Parcelize
    data class Filters(
        private val list: List<FilterUi>
    ) : FilterState {
        override fun dispatch(context: Context, adapter: FilterAdapter) {
            val new: List<FilterUi> = list.map { it.copy() }
            adapter.submitList(new)
        }
    }

    @Parcelize
    data class Error(private val error: String) : FilterState {
        override fun dispatch(context: Context,adapter: FilterAdapter) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()
    }

    @Parcelize
    object Empty : FilterState {
        override fun dispatch(context: Context,adapter: FilterAdapter) = Unit
    }
}