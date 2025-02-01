package com.example.tagplayer.core

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.tagplayer.R

class CustomTextView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private val defaultTextId: Int = R.string.nothing_is_playing

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text?: context.getString(defaultTextId), type)
    }
}