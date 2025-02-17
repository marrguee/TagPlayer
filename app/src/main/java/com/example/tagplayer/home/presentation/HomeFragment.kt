package com.example.tagplayer.home.presentation

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.FragmentHomeBinding
import com.example.tagplayer.main.presentation.BindingFragment
import com.example.tagplayer.tag_settings.presentation.MenuAction

class HomeFragment : BindingFragment<FragmentHomeBinding>() {
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(HomeViewModel::class.java)
    }
    private lateinit var recentlyAdapter: LibraryRecyclerAdapter

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
        recentlyAdapter = LibraryRecyclerAdapter(menuOptions) { id -> viewModel.play(id) }

        with(binding) {
            recentlyRecycler.adapter = recentlyAdapter
            PagerSnapHelper().attachToRecyclerView(recentlyRecycler)
            libraryRecycler.adapter = LibraryRecyclerAdapter(menuOptions) {
                id -> viewModel.play(id)
            }

            tagFilterButton.apply {
                setOnLongClickListener {
                    viewModel.tagSettingsScreen()
                    true
                }
                setOnClickListener {
                    viewModel.filterTagsScreen()
                }
            }

            recentlyTextView.setOnClickListener { viewModel.recentlyPlayedScreen() }

            with(searchView) {
                setOnClickListener { viewModel.searchScreen() }
                setOnQueryTextFocusChangeListener { _, focus: Boolean ->
                    if (focus) {
                        clearFocus()
                        performClick()
                    }
                }
            }

            sortSpinner.adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                R.layout.item_spinner_sort
            ).apply {
                setDropDownViewResource(R.layout.item_spinner_sort_drop_down)
            }

            sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.sortSongs(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.init(SaveRestoreTagFilter(savedInstanceState))
        if (savedInstanceState != null) binding.motionLayout.transitionState = savedInstanceState
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : HomeObserver {
            override fun update(data: HomeState) {
                data.dispatch(requireContext(), recentlyAdapter, binding.libraryRecycler)
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
        outState.putAll(binding.motionLayout.transitionState)
    }
}