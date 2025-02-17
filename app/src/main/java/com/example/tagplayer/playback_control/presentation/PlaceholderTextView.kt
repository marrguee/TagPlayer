package com.example.tagplayer.playback_control.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

abstract class PlaceholderTextView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {
    abstract val defaultTextId: Int

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(
            if(text.isNullOrBlank() && defaultTextId != 0) context.getString(defaultTextId)
            else text,
            type
        )
    }
}