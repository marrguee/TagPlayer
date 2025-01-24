package com.example.tagplayer.playback_control.presentation

import android.os.Bundle
import android.view.View
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.TimeBar
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.PlaybackControlFragmentBinding
import com.example.tagplayer.main.presentation.BindingFragment

@UnstableApi
class PlaybackControlFragment : BindingFragment<PlaybackControlFragmentBinding>() {

    private val viewModel by lazy {
        (requireActivity() as ProvideViewModel).provide(PlaybackControlViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.connectToService(requireContext())
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : PlaybackControlObserver {
            override fun update(data: PlaybackState) {
                with(binding) {
                    data.dispatch(playPause, songTitle, songAuthor, timeBar)
                }
            }
        })

        binding.playPause.setOnClickListener {
            viewModel.playPause()
        }

        binding.resetSong.setOnClickListener {
            viewModel.resetSong()
        }

        binding.timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                if (!canceled) viewModel.seekTo(position)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }
}