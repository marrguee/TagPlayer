package com.example.tagplayer.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

class CustomImageButton@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageButton(context, attrs, defStyleAttr), ModifyCustomImage.Mutable {
    private val imageFacade: ImageFacade = ImageFacade.Base(context)
    private var curImage: CustomImage = CustomImage.Empty(context)
    private val viewRect = Rect()

    init {
        imageFacade.changeImage(curImage)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w <= 0 || h <= 0) return
        with(viewRect){
            left = 0
            top = 0
            right = w
            bottom = h
        }
        imageFacade.prepare(viewRect)
    }

    override fun onDraw(canvas: Canvas) {
        imageFacade.drawOnCanvas(canvas, viewRect, drawable)
    }

    override fun setBackgroundResource(resId: Int) {
        throw IllegalStateException("Background Recourse error! Cannot set background as recourse")
    }

    override fun setBackground(background: Drawable?) {
        curImage = CustomImage.DrawableVariant(context, background)
        imageFacade?.changeImage(CustomImage.DrawableVariant(context, background))
    }

    override fun background(image: CustomImage) {
        curImage = image
        imageFacade?.changeImage(image)
    }

    override fun src(resourceId: Int) {
        setImageResource(resourceId)
    }


}