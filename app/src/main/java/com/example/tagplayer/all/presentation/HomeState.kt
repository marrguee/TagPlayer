package com.example.tagplayer.all.presentation

import com.example.tagplayer.main.presentation.SongUi

interface HomeState {

    fun dispatch(libraryAdapter: LibraryRecyclerAdapter, recentlyAdapter: RecentlyRecyclerAdapter)

    class RecentlyUpdated(
        private val list: List<SongUi>
    ) : HomeState {
        override fun dispatch(
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerAdapter
        ) {
            recentlyAdapter.submitList(list)
        }
    }

    class LibraryUpdated(
        private val list: List<SongUi>,

        ) : HomeState {
        override fun dispatch(
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerAdapter
        ) {
            libraryAdapter.submitList(list)
        }
    }

    class Error(private val msg: String) : HomeState {
        override fun dispatch(
            libraryAdapter: LibraryRecyclerAdapter,
            recentlyAdapter: RecentlyRecyclerAdapter
        ) = Unit
    }
}