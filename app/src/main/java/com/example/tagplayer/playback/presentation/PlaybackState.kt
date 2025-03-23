package com.example.tagplayer.playback.presentation

import android.content.IntentSender
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CustomImage
import com.example.tagplayer.core.presentation.custom_views.interfaces.HandleSeekChanges
import com.example.tagplayer.core.presentation.custom_views.interfaces.ModifyCustomImage
import com.example.tagplayer.core.presentation.custom_views.interfaces.ModifyTextView.Mutable
import com.example.tagplayer.core.presentation.custom_views.interfaces.ModifyTextView.Update
import com.example.tagplayer.playback.presentation.CustomTransitionListener.Disable
import com.example.tagplayer.playback.presentation.CustomTransitionListener.Enable

interface PlaybackState {
    fun dispatch(
        imageButton: ModifyCustomImage.All,
        title: Mutable,
        author: Update,
        seekBar: HandleSeekChanges,
        tagsAdapter: PlaybackAdapter,
        intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
        motionLayout: MotionLayout
    )

    class Connected(private val controller: MediaController) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = controller.run {
            title.run {
                text(mediaMetadata.title)
                scroll(true)
            }
            author.text(mediaMetadata.artist)
            motionLayout.setTransitionListener(if (currentMediaItem == null) Disable else Enable)
            imageButton.run {
                enabled(currentMediaItem != null)
                src(if (playWhenReady) R.drawable.ic_pause else R.drawable.ic_play)
                background(CustomImage.Builder().create(mediaMetadata.artworkData))
                startAnimation()
            }
            seekBar.run {
                enable(currentMediaItem != null)
                duration(duration)
            }
        }
    }

    object Disconnected : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            title.run {
                text()
                scroll(false)
            }
            author.text()
            motionLayout.setTransitionListener(Disable)
            seekBar.run {
                progress(0)
                enable(false)
            }
            imageButton.run {
                src(R.drawable.ic_play)
                background(CustomImage.Empty)
                enabled(false)
                pauseAnimation()
            }
        }
    }

    object StopPlay : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            imageButton.enabled(false)
            motionLayout.setTransitionListener(Disable)
            seekBar.run {
                enable(false)
                duration(0)
            }
        }
    }

    class StartPlay(private val duration: Long) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            imageButton.enabled(true)
            motionLayout.setTransitionListener(Enable)
            seekBar.run {
                enable(true)
                duration(duration)
            }
        }
    }

    class UpdateTags(private val tags: List<TagPlaybackUi>) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = tagsAdapter.submitList(tags)
    }

    class UpdateMetadata(private val metadata: MediaMetadata) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = metadata.let {
            title.text(it.title)
            author.text(it.artist)
            imageButton.background(CustomImage.Builder().create(metadata.artworkData))
        }
    }

    class UpdatePlayPause(private val isPlaying: Boolean) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = imageButton.src(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
    }

    class UpdatePosition(private val position: Long) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = seekBar.progress(position)
    }

    object DeletingSuccess : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = motionLayout.run {
            transitionToStart {
                setTransitionListener(Disable)
            }
        }
    }

    class PermissionRequired(private val intentSender: IntentSender): PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = intentSenderLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
    }

    object ResumeAnimation: PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            title.scroll(true)
            imageButton.startAnimation()
        }
    }

    object PauseAnimation: PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) {
            title.scroll(false)
            imageButton.pauseAnimation()
        }
    }

    class Error(private val error: String): PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = Toast.makeText(motionLayout.context, error, Toast.LENGTH_LONG).show()
    }

    object Empty : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.All,
            title: Mutable,
            author: Update,
            seekBar: HandleSeekChanges,
            tagsAdapter: PlaybackAdapter,
            intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>,
            motionLayout: MotionLayout
        ) = Unit
    }
}