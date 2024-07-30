package com.example.tagplayer.playback_control.presentation

import android.content.Context
import android.widget.SeekBar
import android.widget.TextView
import androidx.media3.common.MediaMetadata
import com.example.tagplayer.R
import com.example.tagplayer.core.ModifyCustomImage
import com.example.tagplayer.core.CustomImage

interface PlaybackControlState {
    fun dispatch(imageButton: ModifyCustomImage.Mutable, title: TextView, author: TextView, seekbar: SeekBar)

    class UpdateSeekbar(
        private val currentPosition: Long,
    ) : PlaybackControlState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView,
            seekbar: SeekBar
        ) {
            seekbar.progress = (currentPosition / 1000).toInt()
        }
    }

    class UpdateMetadata(
        private val context: Context,
        private val duration: Long,
        private val mediaMetadata: MediaMetadata
    ) : PlaybackControlState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView,
            seekbar: SeekBar
        ) {
            seekbar.max = (duration / 1000).toInt()
            imageButton.background(CustomImage.ByteArrayVariant(context, mediaMetadata.artworkData))
            title.text = mediaMetadata.title
            author.text = mediaMetadata.artist
        }
    }

    class UpdatePlayPause(
        private val playWhenReady: Boolean
    ) : PlaybackControlState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView,
            seekbar: SeekBar
        ) {
            imageButton.src(
                if(playWhenReady)
                    R.drawable.ic_pause_with_bg
                else
                    R.drawable.play
            )
        }
    }
}