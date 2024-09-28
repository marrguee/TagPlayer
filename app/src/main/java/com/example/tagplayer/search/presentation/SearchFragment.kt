package com.example.tagplayer.search.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.databinding.SearchFragmentBinding
import com.example.tagplayer.main.presentation.ComebackFragment
import com.example.tagplayer.search.domain.SearchState

class SearchFragment : ComebackFragment<SearchFragmentBinding, SearchViewModel>() {
    private lateinit var adapter: SongSearchListenerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SongSearchListenerAdapter {
            viewModel.playSongForeground(it)
        }

        binding.searchResultRecycler.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false
                viewModel.findSongs(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })



        //viewModel.searchHistory()
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

