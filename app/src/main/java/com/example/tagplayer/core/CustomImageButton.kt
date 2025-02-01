package com.example.tagplayer.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.example.tagplayer.HandleInternalDraw
import com.example.tagplayer.R

class CustomImageButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageButton(context, attrs, defStyleAttr), ModifyCustomImage.Mutable, HandleAnimationCycle {
    private var defBackground: Drawable? = null
    private val imageFacade: ImageFacade = ImageFacade.Base().apply {
        defBackground?.let {
            changeImage(CustomImage.DrawableVariant(it))
        }
    }
    private val viewRect = Rect()
    private val rotatingLines = RotatingLines { invalidate() }

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

    override fun setBackgroundResource(resId: Int) {
        throw IllegalStateException(BACKGROUND_RECOURSE_ERROR)
    }

    override fun setBackground(background: Drawable?) {
        background?.let {
            defBackground = background
        }
    }

    override fun background(image: CustomImage) {
        if (image == CustomImage.Empty)
            defBackground?.let {
                imageFacade.changeImage(CustomImage.DrawableVariant(it))
            }
        else imageFacade.changeImage(image)
        invalidate()
    }

    override fun src(resourceId: Int) {
        setImageResource(resourceId)
    }

    companion object {
        private const val BACKGROUND_RECOURSE_ERROR =
            "Background Recourse error! Cannot set background from xml"
    }

    override fun startAnimation() =
        rotatingLines.startAnimation()

    override fun pauseAnimation() =
        rotatingLines.pauseAnimation()

}