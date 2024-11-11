package com.example.tagplayer.home.presentation

import android.content.Context
import android.widget.Toast
import com.example.tagplayer.core.HideAndShow
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.SongUi

interface HomeState {

    fun dispatch(
        context: Context,
        libraryAdapter: LibraryRecyclerAdapter,
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    class LibraryUpdated(
        private val list: List<SongUi>,
        ) : HomeState {
        override fun dispatch(
            context: Context,
            libraryAdapter: LibraryRecyclerAdapter,
        ) {
            libraryAdapter.submitList(list)
        }

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }

    class Error(private val msg: String) : HomeState {
        override fun dispatch(
            context: Context,
            libraryAdapter: LibraryRecyclerAdapter,
        ) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    object Empty : HomeState {
        override fun dispatch(
            context: Context,
            libraryAdapter: LibraryRecyclerAdapter,
        ) = Unit

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }
}