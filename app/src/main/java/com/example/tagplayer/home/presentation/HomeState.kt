package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.SongUi

interface HomeState {

    fun dispatch(
        libraryAdapter: LibraryRecyclerAdapter,
        recentlyAdapter: RecentlyRecyclerListenerAdapter
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = viewModel.clearObserver()

    class RecentlyUpdated(
        private val list: List<SongUi>
    ) : HomeState {
        override fun dispatch(
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) {
            recentlyAdapter.submitList(list)
        }
    }

    class LibraryUpdated(
        private val list: List<SongUi>,

        ) : HomeState {
        override fun dispatch(
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) {
            libraryAdapter.submitList(list)
        }
    }

    class Error(private val msg: String) : HomeState {
        override fun dispatch(
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) = Unit
    }

    object Empty : HomeState {
        override fun dispatch(
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) = Unit

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObserver) = Unit
    }
}