package com.example.tagplayer.playback_control.presentation

import android.content.Context
import android.widget.SeekBar
import android.widget.TextView
import androidx.media3.common.MediaMetadata
import com.example.tagplayer.R
import com.example.tagplayer.core.ModifyCustomImage
import com.example.tagplayer.core.CustomImage

interface PlaybackControlState {
    fun dispatch(imageButton: ModifyCustomImage.Mutable, title: TextView, author: TextView)

    class UpdateMetadata(
        private val context: Context,
        private val mediaMetadata: MediaMetadata
    ) : PlaybackControlState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView
        ) {
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
            author: TextView
        ) {
            imageButton.src(
                if(playWhenReady)
                    R.drawable.ic_pause_with_bg
                else
                    R.drawable.play
            )
        }
    }

    object Empty : PlaybackControlState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView
        ) = Unit
    }
}