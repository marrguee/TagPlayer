package com.example.tagplayer.filter_by_tags.presentation

import android.os.Bundle
import android.view.View
import com.example.tagplayer.databinding.TagsFilterFragmentScreenBinding
import com.example.tagplayer.main.presentation.ComebackFragment

class FilterFragment : ComebackFragment<TagsFilterFragmentScreenBinding, FilterViewModel>() {
    private lateinit var adapter: FilterAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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