package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.tagplayer.core.presentation.custom_views.interfaces.HideAndShow

class AlphaTextView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), HideAndShow {

    override fun hide() {
        alpha = 0f
    }

    override fun show() {
        alpha = 0.3f
    }
}