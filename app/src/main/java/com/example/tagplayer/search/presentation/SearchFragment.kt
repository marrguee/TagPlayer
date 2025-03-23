package com.example.tagplayer.search.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tagplayer.R
import com.example.tagplayer.databinding.FragmentSearchBinding
import com.example.tagplayer.core.presentation.fragments.ComebackFragment
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction


class SearchFragment : ComebackFragment<FragmentSearchBinding, SearchViewModel>() {
    private lateinit var adapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuOptions = listOf(
            Pair(
                R.id.editSongTagsMenu,
                object : MenuAction {
                    override fun action(vararg args: Any) {
                        viewModel.attachTagsScreen(args[0] as Long)
                    }
                }
            )
        )

        adapter = SearchAdapter(menuOptions) { viewModel.play(it) }
        binding.searchResultRecycler.adapter = adapter

        with(binding.searchView) {
            requestFocus()
            WindowCompat.getInsetsController(requireActivity().window, this)
                .show(WindowInsetsCompat.Type.ime())

            val block: (String?) -> Boolean = { query ->
                if (query.isNullOrEmpty()) {
                    false
                } else {
                    viewModel.search(query.trim())
                    true
                }
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = block.invoke(query)
                override fun onQueryTextChange(newText: String?): Boolean = block.invoke(newText)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : SearchObserver {
            override fun update(data: SearchState) {
                data.dispatch(requireContext(), adapter)
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }
}

