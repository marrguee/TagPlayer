package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.custom_views.interfaces.HandleScrollAnimation
import com.example.tagplayer.core.presentation.custom_views.interfaces.ModifyTextView

class PlaybackTitleTextView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : PlaceholderTextView(context, attrs, defStyleAttr), ModifyTextView.Mutable {
    override val defaultTextId: Int = R.string.placeholder_song_title

    override fun scroll(start: Boolean) {
        isSelected = start
    }
}