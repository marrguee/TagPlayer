package com.example.tagplayer.search.presentation

import android.content.Context
import android.widget.Toast
import com.example.tagplayer.core.domain.HandleUiStateUpdates

interface SearchState {
    fun dispatch(context: Context, searchResultAdapter: SearchAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

    data class SongsSuccess(private val list: List<SearchUi>) : SearchState {
        override fun dispatch(context: Context, searchResultAdapter: SearchAdapter) =
            searchResultAdapter.submitList(list)
    }

    data class Error(private val error: String) : SearchState {
        override fun dispatch(context: Context, searchResultAdapter: SearchAdapter) =
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()
    }

    object Empty : SearchState {
        override fun dispatch(context: Context, searchResultAdapter: SearchAdapter) = Unit
    }
}