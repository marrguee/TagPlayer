package com.example.tagplayer.search.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tagplayer.R
import com.example.tagplayer.databinding.FragmentSearchBinding
import com.example.tagplayer.main.presentation.ComebackFragment
import com.example.tagplayer.search.domain.SearchState
import com.example.tagplayer.tag_settings.presentation.MenuAction


class SearchFragment : ComebackFragment<FragmentSearchBinding, SearchViewModel>() {
    private lateinit var adapter: SongSearchListenerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuOptions = listOf(
            Pair(
                R.id.editSongTagsMenu,
                object : MenuAction {
                    override fun action(vararg args: Any) {
                        viewModel.editSongTagsScreen(args[0] as Long)
                    }
                }
            )
        )

        adapter = SongSearchListenerAdapter(menuOptions) { viewModel.playSongForeground(it) }
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
                    if (newText.isNullOrEmpty()) return false
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

