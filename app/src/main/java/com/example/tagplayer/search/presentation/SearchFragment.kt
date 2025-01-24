package com.example.tagplayer.search.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tagplayer.R
import com.example.tagplayer.databinding.SearchFragmentBinding
import com.example.tagplayer.main.presentation.ComebackFragment
import com.example.tagplayer.search.domain.SearchState


class SearchFragment : ComebackFragment<SearchFragmentBinding, SearchViewModel>() {
    private lateinit var adapter: SongSearchListenerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SongSearchListenerAdapter { viewModel.playSongForeground(it) }
        binding.searchResultRecycler.adapter = adapter

        with(binding.searchView) {
            requestFocus()
            WindowCompat.getInsetsController(requireActivity().window, this)
                .show(WindowInsetsCompat.Type.ime())

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query.isNullOrEmpty()) return false
                    viewModel.findSongs(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty() || newText.length < 3) return false
                    viewModel.findSongs(newText)
                    return true
                }
            })
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : SearchObserver {
            override fun update(data: SearchState) {
                data.dispatch(adapter)
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }
}

