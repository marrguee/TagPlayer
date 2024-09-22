package com.example.tagplayer.filter_by_tags

import android.os.Bundle
import android.view.View
import com.example.tagplayer.databinding.TagsFilterFragmentScreenBinding
import com.example.tagplayer.main.presentation.ComebackFragment

class FilterTagsFragment : ComebackFragment<TagsFilterFragmentScreenBinding, FilterTagsViewModel>() {

    private lateinit var adapter: FilterTagsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FilterTagsAdapter {
            viewModel.changeTagSelectedState(it)
        }

        binding.tagsRecyclerView.adapter = adapter

        binding.applyFiltersButton.setOnClickListener {
            viewModel.applyFilter()
        }

        binding.tagFilterButton.setOnClickListener {
            viewModel.comeback()
        }

        viewModel.init()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : FilterTagObserver {
            override fun update(data: TagsFilterState) {
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