package com.example.tagplayer.search.domain

import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.search.presentation.SongSearchListenerAdapter
import com.example.tagplayer.search.presentation.SongSearchUi

interface SearchState {
    fun dispatch(searchResultAdapter: SongSearchListenerAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = viewModel.clearObserver()

    class SongsSuccess(private val list: List<SongSearchUi>) : SearchState {
        override fun dispatch(searchResultAdapter: SongSearchListenerAdapter) {
            searchResultAdapter.submitList(list)
        }
    }

    class Error(private val cause: String) : SearchState {
        override fun dispatch(searchResultAdapter: SongSearchListenerAdapter) = Unit
    }

    object Empty : SearchState {
        override fun dispatch(searchResultAdapter: SongSearchListenerAdapter) = Unit
    }
}