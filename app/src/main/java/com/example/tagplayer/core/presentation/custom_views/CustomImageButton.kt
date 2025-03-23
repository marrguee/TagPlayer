package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.CustomImage
import com.example.tagplayer.core.domain.CustomImage.*
import com.example.tagplayer.core.presentation.custom_views.interfaces.ImageFacade
import com.example.tagplayer.core.presentation.custom_views.interfaces.ModifyCustomImage

class CustomImageButton@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageButton(context, attrs, defStyleAttr), ModifyCustomImage.All {
    private val viewRect = Rect()
    private val rotatingLines = RotatingLines { invalidate() }
    private var placeholder: CustomImage = DrawableImage(
        ContextCompat.getDrawable(context, R.drawable.placeholder_playback)!!
    )
    private val imageFacade: ImageFacade = ImageFacade.Base(placeholder)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w <= 0 || h <= 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        rotatingLines.onSizeChanged(viewRect)
        imageFacade.prepare(rotatingLines)
    }

    override fun onDraw(canvas: Canvas) {
        rotatingLines.onDraw(canvas, width, height)
        imageFacade.drawOnCanvas(canvas, rotatingLines, drawable)
    }

    override fun setBackgroundResource(resId: Int) = setBackground(null)

    override fun setBackground(background: Drawable?) =
        throw IllegalStateException(BACKGROUND_RECOURSE_ERROR)

    override fun background(image: CustomImage) {
        imageFacade.changeImage(if (image == Empty) placeholder else image)
        invalidate()
    }

    override fun src(resourceId: Int) = setImageResource(resourceId)

    override fun enabled(enabled: Boolean) {
        isEnabled = enabled
    }

    override fun startAnimation() = rotatingLines.startAnimation()

    override fun pauseAnimation() = rotatingLines.pauseAnimation()

    companion object {
        private const val BACKGROUND_RECOURSE_ERROR =
            "Background Recourse error! Cannot set background from xml"
    }

}