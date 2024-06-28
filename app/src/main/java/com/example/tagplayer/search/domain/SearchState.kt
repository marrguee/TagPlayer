package com.example.tagplayer.search.domain

import android.widget.ArrayAdapter
import com.example.tagplayer.all.presentation.SongUi
import com.example.tagplayer.search.presentation.SearchAdapter
import com.example.tagplayer.search.presentation.SearchUi

interface SearchState {
    fun dispatch(adapter: ArrayAdapter<SearchUi>, searchResultAdapter: SearchAdapter)

    class HistorySuccess(private val list: List<SearchUi>) : SearchState {
        override fun dispatch(adapter: ArrayAdapter<SearchUi>, searchResultAdapter: SearchAdapter) {
            adapter.clear()
            adapter.addAll(list)
        }
    }

    class SongsSuccess(private val list: List<SongUi>) : SearchState {
        override fun dispatch(adapter: ArrayAdapter<SearchUi>, searchResultAdapter: SearchAdapter) {
            searchResultAdapter.submitList(list)
        }
    }

    class Error(private val cause: String) : SearchState {
        override fun dispatch(adapter: ArrayAdapter<SearchUi>, searchResultAdapter: SearchAdapter) = Unit
    }
}