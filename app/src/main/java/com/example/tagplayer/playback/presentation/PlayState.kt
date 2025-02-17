package com.example.tagplayer.playback.presentation

import android.content.IntentSender
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.media3.common.MediaMetadata
import com.example.tagplayer.R
import com.example.tagplayer.core.CustomImage
import com.example.tagplayer.core.ModifyCustomImage

interface PlayState {
    fun dispatch(
        imageButton: ModifyCustomImage.All,
        title: TextView,
        author: TextView,
        timeBar: SeekBar,
        tagAdapter: TagsPlaybackAdapter,
        intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
        motionLayout: MotionLayout
    )

    class UpdateTags(
        private val tags: List<TagPlaybackUi>
    ) : PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            tagAdapter.submitList(tags)
        }
    }

    class UpdateMetadata(
        private val mediaMetadata: MediaMetadata
    ) : PlayState {

        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            imageButton.background(
                if (mediaMetadata.artworkData != null)
                    CustomImage.ByteArrayVariant(mediaMetadata.artworkData!!)
                else
                    CustomImage.Empty
            )
            title.text = mediaMetadata.title
            author.text = mediaMetadata.artist
        }
    }

    class UpdatePlayPause(
        private val isPlaying: Boolean
    ) : PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            imageButton.src(
                if (isPlaying)
                    R.drawable.ic_pause_with_bg
                else
                    R.drawable.ic_play_with_bg
            )
        }
    }

    class UpdateDuration(private val duration: Long) : PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            timeBar.max = (duration / 1000).toInt()
        }
    }

    class UpdatePosition(private val position: Long) : PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            timeBar.progress = (position / 1000).toInt()
        }
    }

    class UpdateTimeBarEnabled(private val enable: Boolean) : PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            timeBar.isEnabled = enable
        }
    }

    class ChangePlayPauseEnabled(private val isConnected: Boolean) : PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            imageButton.enabled(isConnected)
        }
    }

    class PermissionRequired(private val intentSender: IntentSender): PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            intentSenderLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
        }
    }

    class Error(private val error: String): PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {

        }
    }

    object EnableMotion: PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {}

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {}

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {}
            })
        }
    }

    object DisableMotion: PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {
                    motionLayout?.progress = 0f
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {}

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {}
            })
        }
    }

    object DeletingSuccess : PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            motionLayout.transitionToStart()
        }
    }
    
    object StartAnimation: PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            title.isSelected = true
            imageButton.startAnimation()
        }
    }

    object PauseAnimation: PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            title.isSelected = false
            imageButton.pauseAnimation()
        }
    }

    object Empty : PlayState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: TextView,
            author: TextView,
            timeBar: SeekBar,
            tagAdapter: TagsPlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = Unit
    }
}