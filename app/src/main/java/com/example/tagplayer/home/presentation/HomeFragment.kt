package com.example.tagplayer.home.presentation

import android.os.Bundle
import android.view.View
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.HomeFragmentScreenBinding
import com.example.tagplayer.main.presentation.BindingFragment
import com.example.tagplayer.tag_settings.presentation.MenuAction

class HomeFragment : BindingFragment<HomeFragmentScreenBinding>() {
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(HomeViewModel::class.java)
    }
    private lateinit var libraryAdapter: LibraryRecyclerAdapter

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

        libraryAdapter = LibraryRecyclerAdapter(menuOptions) { id ->
            viewModel.play(id)
        }
        binding.libraryRecycler.adapter = libraryAdapter

        binding.tagFilterButton.apply {
            setOnLongClickListener {
                viewModel.tagSettingsScreen()
                true
            }
            setOnClickListener {
                viewModel.filterTagsScreen()
            }
        }

        binding.recentlyButton.setOnClickListener {
            viewModel.recentlyPlayedScreen()
        }

        binding.searchView.setOnClickListener {
            viewModel.searchScreen()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.init(SaveRestoreTagFilter(savedInstanceState))
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : HomeObserver {
            override fun update(data: HomeState) {
                data.dispatch(requireContext(), libraryAdapter)
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(SaveRestoreTagFilter(outState))
    }
}