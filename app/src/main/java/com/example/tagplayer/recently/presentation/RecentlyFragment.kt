package com.example.tagplayer.recently.presentation

import android.os.Bundle
import android.view.View
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.databinding.RecentlyFragmentScreenBinding
import com.example.tagplayer.main.presentation.ComebackFragment
import com.example.tagplayer.playback_control.presentation.PlaybackControlFragment

class RecentlyFragment : ComebackFragment<RecentlyFragmentScreenBinding, RecentlyViewModel>() {
    private lateinit var recyclerView: RecyclerView

    @UnstableApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecentlyListenerAdapter {
            viewModel.play(it)
        }
        binding.recentlyRecycler.adapter = adapter

        viewModel.recently()

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.playbackRecentlyControlContainer, PlaybackControlFragment())
                .commit()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : RecentlyObserver {
            override fun update(data: RecentlyState) {
                data.dispatch(recyclerView)
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }
}