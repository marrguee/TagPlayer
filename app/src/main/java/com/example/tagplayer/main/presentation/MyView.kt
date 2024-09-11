package com.example.tagplayer.main.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

interface MyView {
    fun title(title: String)
    fun color(resBackgroundId: Int)
}

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), MyView {

    override fun title(title: String) {
        text = title
    }

    override fun color(resBackgroundId: Int) {
        setBackgroundResource(resBackgroundId)
    }
}