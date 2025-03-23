package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import com.example.tagplayer.R

class PlaybackAuthorTextView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : PlaceholderTextView(context, attrs, defStyleAttr) {
    override val defaultTextId: Int = R.string.placeholder_song_author
}