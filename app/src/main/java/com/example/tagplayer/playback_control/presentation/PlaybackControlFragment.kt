package com.example.tagplayer.playback_control.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.R
import com.example.tagplayer.core.CustomImageButton
import com.example.tagplayer.core.domain.ProvideViewModel

class PlaybackControlFragment : Fragment(R.layout.playback_tracking_fragment) {
    private val viewModel by lazy {
        (requireActivity() as ProvideViewModel).provide(PlaybackControlViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val motionLayout = view.findViewById<MotionLayout>(R.id.motionLayout)
        motionLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (motionLayout.currentState == R.id.start) {
                    motionLayout.transitionToEnd()
                } else if(motionLayout.currentState == R.id.end) {
                    motionLayout.transitionToStart()
                }
                true
            } else {
                false
            }
        }
        motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                motionLayout?.isInteractionEnabled = false
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                motionLayout?.isInteractionEnabled = true
            }
        })

        val imageButton = view.findViewById<CustomImageButton>(R.id.playPause)
        val songTitle = view.findViewById<TextView>(R.id.songTitle)
        val songAuthor = view.findViewById<TextView>(R.id.songAuthor)
        val seekbar = view.findViewById<SeekBar>(R.id.seekbar)
        viewModel.observe(this) {
            it.dispatch(imageButton, songTitle, songAuthor, seekbar)
        }
    }

    @UnstableApi
    override fun onStart() {
        super.onStart()
        viewModel.connectToService(requireContext())
    }
}