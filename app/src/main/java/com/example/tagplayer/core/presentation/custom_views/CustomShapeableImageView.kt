package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import com.example.tagplayer.GlideApp
import com.example.tagplayer.R
import com.example.tagplayer.core.presentation.custom_views.interfaces.MyView
import com.google.android.material.imageview.ShapeableImageView

class CustomShapeableImageView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): ShapeableImageView(context, attrs, defStyleAttr), MyView {

    override fun title(title: String) = Unit

    override fun color(resId: Int, color: String) = Unit

    override fun image(bitmap: String?) {
        GlideApp.with(context)
            .load(Uri.parse(bitmap))
            .error(R.drawable.placeholder_song)
            .into(this)
    }
}