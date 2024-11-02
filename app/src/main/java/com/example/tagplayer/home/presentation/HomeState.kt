package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.HideAndShow
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.SongUi

interface HomeState {

    fun dispatch(
        recentlyTextView: HideAndShow,
        libraryAdapter: LibraryRecyclerAdapter,
        recentlyAdapter: RecentlyRecyclerListenerAdapter
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    class RecentlyUpdated(
        private val list: List<SongUi>
    ) : HomeState {
        override fun dispatch(
            recentlyTextView: HideAndShow,
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) {
            recentlyTextView.show()
            recentlyAdapter.submitList(list)
        }
    }

    object HideRecently : HomeState {
        override fun dispatch(
            recentlyTextView: HideAndShow,
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) {
            recentlyTextView.hide()
        }
    }

    class LibraryUpdated(
        private val list: List<SongUi>,

        ) : HomeState {
        override fun dispatch(
            recentlyTextView: HideAndShow,
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) {
            libraryAdapter.submitList(list)
        }
    }

    class Error(private val msg: String) : HomeState {
        override fun dispatch(
            recentlyTextView: HideAndShow,
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) = Unit
    }

    object Empty : HomeState {
        override fun dispatch(
            recentlyTextView: HideAndShow,
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerListenerAdapter
        ) = Unit

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }
}