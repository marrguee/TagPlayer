package com.example.tagplayer.recently.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.playback_control.presentation.PlaybackControlFragment

class RecentlyFragment : Fragment(R.layout.recently_fragment_screen) {

    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(RecentlyViewModel::class.java)
    }

    @UnstableApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecentlyAdapter {
            viewModel.play(it)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recentlyRecycler)
        recyclerView.adapter = adapter

        viewModel.observe(this) {
            it.dispatch(recyclerView)
        }
        viewModel.recently()

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.playbackRecentlyControlContainer, PlaybackControlFragment())
                .commit()
        }

    }
}