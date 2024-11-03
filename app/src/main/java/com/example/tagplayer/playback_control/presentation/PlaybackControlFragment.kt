package com.example.tagplayer.playback_control.presentation

import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.PlaybackControlFragmentBinding
import com.example.tagplayer.main.presentation.BindingFragment

class PlaybackControlFragment : BindingFragment<PlaybackControlFragmentBinding>() {

    private val viewModel by lazy {
        (requireActivity() as ProvideViewModel).provide(PlaybackControlViewModel::class.java)
    }

    @UnstableApi
    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : PlaybackControlObserver {
            override fun update(data: PlaybackControlState) {
                with(binding) {
                    data.dispatch(playPause, songTitle, songAuthor)
                }
            }
        })
        viewModel.connectToService(requireContext())
        binding.playPause.setOnClickListener {
            viewModel.playPause()
        }
        binding.resetSong.setOnClickListener {
            viewModel.resetSong()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }
}

interface PlaybackControlObserver : CustomObserver<PlaybackControlState> {
    object Empty : PlaybackControlObserver {
        override fun update(data: PlaybackControlState) = Unit
    }
}