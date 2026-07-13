package com.example.tagplayer.filter.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.tagplayer.core.presentation.fragments.ComebackFragment
import com.example.tagplayer.databinding.FragmentTagsFilterBinding

class FilterFragment : ComebackFragment<FragmentTagsFilterBinding, FilterViewModel>() {
    private lateinit var adapter: FilterAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { _, _ -> true }
        adapter = FilterAdapter { viewModel.apply(it) }

        with(binding) {
            tagsRecyclerView.adapter = adapter
            tagFilterButton.setOnClickListener { viewModel.comeback() }
            clearFiltersButton.setOnClickListener { viewModel.reset() }
        }

        viewModel.init()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : FilterObserver {
            override fun update(data: FilterState) {
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