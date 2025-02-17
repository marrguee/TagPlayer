package com.example.tagplayer.core

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.tagplayer.GlideApp
import com.example.tagplayer.R
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            .placeholder(R.drawable.placeholder_loading)
            .error(R.drawable.placeholder_playback)
            .into(this)
    }
}