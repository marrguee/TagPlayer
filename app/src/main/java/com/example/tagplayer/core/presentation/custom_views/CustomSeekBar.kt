package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import com.example.tagplayer.core.presentation.custom_views.interfaces.HandleSeekChanges

class CustomSeekBar@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatSeekBar(context, attrs, defStyleAttr), HandleSeekChanges {
    override fun progress(position: Long) {
        progress = (position / 1000).toInt()
    }

    override fun duration(duration: Long) {
        max = (duration / 1000).toInt()
    }

    override fun enable(enable: Boolean) {
        isEnabled = enable
    }
}