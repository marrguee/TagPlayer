package com.example.tagplayer.home.presentation

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.FragmentHomeBinding
import com.example.tagplayer.core.presentation.fragments.BindingFragment
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction

class HomeFragment : BindingFragment<FragmentHomeBinding>() {
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(HomeViewModel::class.java)
    }
    private lateinit var recentlyAdapter: HomeAdapter

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
        recentlyAdapter = HomeAdapter(menuOptions) { viewModel.play(it) }

        with(binding) {
            recentlyRecycler.adapter = recentlyAdapter
            PagerSnapHelper().attachToRecyclerView(recentlyRecycler)
            libraryRecycler.adapter = HomeAdapter(menuOptions) { id ->
                viewModel.play(id)
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

            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) viewModel.searchScreen()
            }

            sortSpinner.adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                R.layout.item_spinner_sort
            ).apply {
                setDropDownViewResource(R.layout.item_spinner_sort_drop_down)
            }

            sortSpinner.onItemSelectedListener = SortingTypeListener.Base(viewModel)
        }

        viewModel.loadRecently()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) binding.motionLayout.transitionState = savedInstanceState
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : HomeObserver {
            override fun update(data: HomeState) {
                data.dispatch(
                    binding.libraryPlaceholder,
                    recentlyAdapter,
                    binding.libraryRecycler,
                    binding.motionLayout
                )
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
        outState.putAll(binding.motionLayout.transitionState)
    }
}