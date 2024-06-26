package com.example.tagplayer.history.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel

class HistoryFragment : Fragment(R.layout.history_fragment) {

    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(HistoryViewModel::class.java)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HistoryRecyclerAdapter {
            viewModel.play(it)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.historyRecycler)
        recyclerView.adapter = adapter

        viewModel.observe(this) {
            it.dispatch(adapter)
        }
        viewModel.history()
    }
}