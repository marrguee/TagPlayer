package com.example.tagplayer.filter_by_tags.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.tagplayer.databinding.FragmentTagsFilterBinding
import com.example.tagplayer.main.presentation.ComebackFragment

class FilterFragment : ComebackFragment<FragmentTagsFilterBinding, FilterViewModel>() {
    private lateinit var adapter: FilterAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { _, _ -> true }
        adapter = FilterAdapter {
            viewModel.changeTagSelectedState(it)
        }

        binding.tagsRecyclerView.adapter = adapter

        binding.applyFiltersButton.setOnClickListener {
            viewModel.applyFilter()
        }

        binding.tagFilterButton.setOnClickListener {
            viewModel.comeback()
        }

        binding.clearFiltersButton.setOnClickListener {
            viewModel.clearFilter()
        }

        viewModel.init(SaveRestoreFilterState(savedInstanceState))
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : FilterObserver {
            override fun update(data: FilterScreenState) {
                data.dispatch(adapter)
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
        viewModel.save(SaveRestoreFilterState(outState))
    }
}