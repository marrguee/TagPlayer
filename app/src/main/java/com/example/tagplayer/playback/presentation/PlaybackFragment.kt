package com.example.tagplayer.playback.presentation

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.FragmentPlaybackControlBinding
import com.example.tagplayer.core.presentation.fragments.BindingFragment

@UnstableApi
class PlaybackFragment : BindingFragment<FragmentPlaybackControlBinding>() {
    private lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var tagsAdapter: PlaybackAdapter
    private val viewModel by lazy {
        (requireActivity() as ProvideViewModel).provide(PlaybackViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            val context = requireContext()
            val toastText = ContextCompat.getString(
                context,
                if (it.resultCode == RESULT_OK) {
                    viewModel.deleteSong()
                    viewModel.clearMediaQueue()
                    R.string.song_delete_success
                } else {
                    R.string.song_delete_failed
                }
            )
            Toast.makeText(
                context,
                toastText,
                Toast.LENGTH_SHORT
            ).show()
        }
        tagsAdapter = PlaybackAdapter()
        with(binding) {
            songTagsRecycler.adapter = tagsAdapter
            playPause.setOnClickListener { viewModel.playPause() }
            rewindSong.setOnClickListener { viewModel.rewind() }
            tagsButton.setOnClickListener { viewModel.editTags() }
            shareButton.setOnClickListener { viewModel.shareSong() }
            trashButton.setOnClickListener { viewModel.deleteSong() }
            seekBar.setOnSeekBarChangeListener(SimpleSeekBarListener(viewModel))
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) binding.motionLayout.transitionState = savedInstanceState
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : PlaybackObserver {
            override fun update(data: PlaybackState) {
                with(binding) {
                    data.dispatch(
                        playPause,
                        songTitle,
                        songAuthor,
                        seekBar,
                        tagsAdapter,
                        launcher,
                        motionLayout
                    )
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(binding.motionLayout.transitionState)
    }
}