package com.example.tagplayer.all.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel

class AllFragment : Fragment(R.layout.all_fragment) {
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(AllFeatureViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.trackRecycler)
        val adapter = AllRecyclerAdapter { id ->
            viewModel.play(id)
        }
        recyclerView.adapter = adapter

        viewModel.observe(this) {
            it.dispatch(adapter)
        }

        viewModel.loadTracks()
        viewModel.scan()

    }
}