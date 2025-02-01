package com.example.tagplayer.playback_control.presentation

import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.TimeBar
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.PlaybackControlFragmentBinding
import com.example.tagplayer.main.presentation.BindingFragment

@UnstableApi
class PlaybackControlFragment : BindingFragment<PlaybackControlFragmentBinding>() {

    private val viewModel by lazy {
        (requireActivity() as ProvideViewModel).provide(PlaybackControlViewModel::class.java)
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
        viewModel.connectService(requireContext())

        binding.playPause.setOnClickListener { viewModel.playPause() }
        binding.rewindSong.setOnClickListener { viewModel.rewindSong() }

        binding.timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                if (!canceled) viewModel.seekTo(position)
            }
        })

        binding.playPause.startAnimation()

    }

    override fun onPause() {
        super.onPause()
        viewModel.disconnectService()
        viewModel.stopGettingUpdates()
        binding.playPause.pauseAnimation()
    }
}