package com.example.tagplayer.home.presentation

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.SongUi

interface HomeState {

    fun dispatch(
        context: Context,
        recentlyAdapter: LibraryRecyclerAdapter,
        libraryRecyclerView: RecyclerView
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    class LibraryUpdated(
        private val list: List<SongUi>,
        ) : HomeState {
        override fun dispatch(
            context: Context,
            recentlyAdapter: LibraryRecyclerAdapter,
            libraryRecyclerView: RecyclerView
        ) {
            val layoutManager = (libraryRecyclerView.layoutManager as LinearLayoutManager)
            val scrollPosition = layoutManager.findFirstVisibleItemPosition()
            (libraryRecyclerView.adapter as LibraryRecyclerAdapter).submitList(list) {
                if (scrollPosition != RecyclerView.NO_POSITION) {
                    layoutManager.scrollToPosition(scrollPosition)
                }
            }
        }

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }

    class RecentlyUpdated(
        private val list: List<SongUi>,
    ) : HomeState {
        override fun dispatch(
            context: Context,
            recentlyAdapter: LibraryRecyclerAdapter,
            libraryRecyclerView: RecyclerView
        ) {
            recentlyAdapter.submitList(list)
        }

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }

    class Error(private val msg: String) : HomeState {
        override fun dispatch(
            context: Context,
            recentlyAdapter: LibraryRecyclerAdapter,
            libraryRecyclerView: RecyclerView
        ) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    object Empty : HomeState {
        override fun dispatch(
            context: Context,
            recentlyAdapter: LibraryRecyclerAdapter,
            libraryRecyclerView: RecyclerView
        ) = Unit

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }
}