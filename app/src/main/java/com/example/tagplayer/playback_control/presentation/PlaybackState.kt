package com.example.tagplayer.playback_control.presentation

import android.content.Context
import android.widget.TextView
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.DefaultTimeBar
import com.example.tagplayer.R
import com.example.tagplayer.core.ModifyCustomImage
import com.example.tagplayer.core.CustomImage

@UnstableApi
interface PlaybackState {
    fun dispatch(imageButton: ModifyCustomImage.Mutable, title: TextView, author: TextView, timeBar: DefaultTimeBar)

    class UpdateMetadata(
        private val context: Context,
        private val mediaMetadata: MediaMetadata
    ) : PlaybackState {

        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView,
            timeBar: DefaultTimeBar
        ) {
            imageButton.background(CustomImage.ByteArrayVariant(context, mediaMetadata.artworkData))
            title.text = mediaMetadata.title
            author.text = mediaMetadata.artist
        }
    }

    class UpdatePlayPause(
        private val isPlaying: Boolean
    ) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView,
            timeBar: DefaultTimeBar
        ) {
            imageButton.src(
                if(isPlaying)
                    R.drawable.ic_pause_with_bg
                else
                    R.drawable.play
            )
        }
    }

    class UpdateDuration(private val duration: Long) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView,
            timeBar: DefaultTimeBar
        ) {
            timeBar.setDuration(duration)
        }
    }

    class UpdatePosition(private val position: Long) : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView,
            timeBar: DefaultTimeBar
        ) {
            timeBar.setPosition(position)
        }
    }

    object Empty : PlaybackState {
        override fun dispatch(
            imageButton: ModifyCustomImage.Mutable,
            title: TextView,
            author: TextView,
            timeBar: DefaultTimeBar
        ) = Unit
    }
}