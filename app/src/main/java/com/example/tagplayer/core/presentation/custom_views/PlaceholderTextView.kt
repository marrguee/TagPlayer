package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.tagplayer.core.presentation.custom_views.interfaces.ModifyTextView
import com.example.tagplayer.core.presentation.custom_views.interfaces.UpdateText

abstract class PlaceholderTextView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr), ModifyTextView.Update {
    abstract val defaultTextId: Int

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(
            if(text.isNullOrBlank() && defaultTextId != 0) context.getString(defaultTextId)
            else text,
            type
        )
    }

    override fun text(text: CharSequence?) {
        setText(text, BufferType.NORMAL)
    }
}