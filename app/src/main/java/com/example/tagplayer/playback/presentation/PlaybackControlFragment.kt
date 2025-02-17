package com.example.tagplayer.playback.presentation

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.databinding.FragmentPlaybackControlBinding
import com.example.tagplayer.main.presentation.BindingFragment

@UnstableApi
class PlaybackControlFragment : BindingFragment<FragmentPlaybackControlBinding>() {
    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var tagsAdapter: TagsPlaybackAdapter
    private val viewModel by lazy {
        (requireActivity() as ProvideViewModel).provide(PlaybackViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intentSenderLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                val context = requireContext()
                val toastText = ContextCompat.getString(
                    context,
                    if (it.resultCode == RESULT_OK) {
                        viewModel.deleteSong()
                        viewModel.clearBeforeDeleting()
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
        tagsAdapter = TagsPlaybackAdapter()
        binding.songTagsRecycler.adapter = tagsAdapter

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) binding.motionLayout.transitionState = savedInstanceState
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : PlaybackControlObserver {
            override fun update(data: PlayState) {
                with(binding) {
                    data.dispatch(
                        playPause,
                        songTitle,
                        songAuthor,
                        timeBar,
                        tagsAdapter,
                        intentSenderLauncher,
                        motionLayout
                    )
                }
            }
        })

        viewModel.connectService(requireContext())

        with(binding) {
            motionLayout.enableTransition(R.id.playbackTransition, false)
            playPause.setOnClickListener { viewModel.playPause() }
            rewindSong.setOnClickListener { viewModel.rewindSong() }

            timeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) = Unit

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        viewModel.seekTo((it.progress * 1000).toLong())
                    }
                }

            })

            editTagsButton.setOnClickListener {
                viewModel.editSongTagsScreen()
            }

            shareButton.setOnClickListener {
                viewModel.shareSong(requireContext())
            }
            trashButton.setOnClickListener {
                viewModel.deleteSong()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.disconnectService()
        viewModel.stopGettingUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(binding.motionLayout.transitionState)
    }
}