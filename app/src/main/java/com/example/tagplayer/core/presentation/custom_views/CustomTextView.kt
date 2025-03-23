package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.custom_views.interfaces.MyView

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), MyView {

    override fun title(title: String) {
        text = title
    }

    override fun color(resId: Int, color: String) {
        val background = ContextCompat.getDrawable(context, resId) as? GradientDrawable
        if (background != null) {
            background.let {
                try {
                    if (color.isEmpty()) throw IllegalArgumentException()
                    it.setColor(Color.parseColor(color))
                } catch (e: IllegalArgumentException) {
                    it.setColor(ContextCompat.getColor(context, R.color.tag_selected))
                }
                setBackground(it)
            }
        } else {
            setBackgroundResource(resId)
        }
    }

    override fun image(bitmap: String?) = Unit
}