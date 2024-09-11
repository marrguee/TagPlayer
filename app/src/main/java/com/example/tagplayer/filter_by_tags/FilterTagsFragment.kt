package com.example.tagplayer.filter_by_tags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.TagsFilterFragmentScreenBinding

class FilterTagsFragment : Fragment() {
    private lateinit var binding: TagsFilterFragmentScreenBinding
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(FilterTagsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TagsFilterFragmentScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FilterTagsAdapter {
            viewModel.changeTagSelectedState(it)
        }
        viewModel.observe(this) {
            it.dispatch(adapter)
        }
        binding.tagsRecyclerView.adapter = adapter

        binding.applyFiltersButton.setOnClickListener {
            viewModel.applyFilter()
        }

        binding.tagFilterButton.setOnClickListener {
            viewModel.pop()
        }

        viewModel.init()
    }
}