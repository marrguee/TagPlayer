package com.example.tagplayer.tag_details.presentation.radio_grid

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.custom_views.interfaces.SelectAndUnselect
import com.google.android.material.imageview.ShapeableImageView

class PickerImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ShapeableImageView(context, attrs, defStyleAttr), SelectAndUnselect, ProvideColor {
    private val hexColor: String
    private var selected: Boolean

    init {
        val ta = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.pickedColor))
        val colorInt = ta.getColor(0, Color.LTGRAY)
        ta.recycle()
        setBackgroundColor(colorInt)
        hexColor = String.format("#%06X", (0xFFFFFF and colorInt))
        selected = false
    }

    override fun select() {
        setImageResource(R.drawable.ic_check)
        selected = true
    }

    override fun unselect() {
        setImageResource(0)
        selected = false
    }

    override fun selected(): Boolean = selected

    override fun color(): String = hexColor
}