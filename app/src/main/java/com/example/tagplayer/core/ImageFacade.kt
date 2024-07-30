package com.example.tagplayer.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import com.example.tagplayer.R
import kotlin.math.abs

interface ImageFacade {
    fun changeImage(newImage: CustomImage)
    fun drawOnCanvas(canvas: Canvas, viewRect: Rect, src: Drawable)
    fun prepare(viewRect: Rect)

    class Base(context: Context) : ImageFacade {
        private var image: CustomImage = CustomImage.Empty(context)

        private val maskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.FILL
        }
        private lateinit var maskBm: Bitmap
        private lateinit var resultBm: Bitmap
        private lateinit var srcBounds: Rect

        private lateinit var zoomedViewRect: Rect

        override fun changeImage(newImage: CustomImage) {
            image = newImage
        }

        override fun drawOnCanvas(canvas: Canvas, viewRect: Rect, src: Drawable) {
            Canvas(maskBm).drawOval(viewRect.toRectF(), maskPaint)
            val resultCanvas = Canvas(resultBm)
            resultCanvas.drawBitmap(maskBm, viewRect, viewRect, null)
            image.drawOnCanvas(resultCanvas, zoomedViewRect)
            canvas.drawBitmap(resultBm, viewRect, viewRect, null)
            src.bounds = srcBounds
            src.draw(canvas)
        }

        override fun prepare(viewRect: Rect) {
            maskBm = Bitmap.createBitmap(
                viewRect.width(),
                viewRect.height(),
                Bitmap.Config.ALPHA_8
            )
            resultBm = maskBm.copy(Bitmap.Config.ARGB_8888, true)

            val halfHeight = (viewRect.height() / 2 * SCALE).toInt()
            val halfWidth = (viewRect.width() / 2 * SCALE).toInt()
            srcBounds = Rect(
                viewRect.left + halfHeight,
                viewRect.top + halfHeight,
                viewRect.right - halfWidth,
                viewRect.bottom - halfWidth,
            )

            val zoomStep = (viewRect.width() * ZOOM).toInt()
            zoomedViewRect = Rect(
                viewRect.left - zoomStep,
                viewRect.top - zoomStep,
                viewRect.right + zoomStep,
                viewRect.bottom + zoomStep
            )
        }

        companion object {
            private const val SCALE = 0.4f
            private const val ZOOM = 0.4f
        }
    }

}

abstract class CustomImage(context: Context) {
    protected var imageBm: Bitmap
    private val imageBmBounds: Rect
    private val imageBmPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    init {
        imageBm = ContextCompat.getDrawable(
            context,
            R.drawable.music_splash
        )!!.run {
            toBitmap(
                minimumWidth,
                minimumHeight,
                Bitmap.Config.ARGB_8888
            )
        }

        if (imageBm.width != imageBm.height) {
            val indent = abs(imageBm.width - imageBm.height) / 2

            imageBm = if (imageBm.width > imageBm.height) {
                Bitmap.createBitmap(imageBm, indent, 0, imageBm.height, imageBm.height)
            } else {
                Bitmap.createBitmap(imageBm, 0, indent, imageBm.width, imageBm.width)
            }
        }

        imageBmBounds = Rect(
            0, 0,
            imageBm.width, imageBm.height
        )
    }

    fun drawOnCanvas(canvas: Canvas, zoomedViewRect: Rect) {
        canvas.drawBitmap(imageBm, imageBmBounds, zoomedViewRect, imageBmPaint)
    }

    class ByteArrayVariant(context: Context, byteArray: ByteArray?) : CustomImage(context) {
        init {
            if(byteArray != null) {
                imageBm = BitmapFactory.decodeByteArray(
                    byteArray,
                    0,
                    byteArray.size
                )
            }
        }
    }

    class DrawableVariant(context: Context, drawable: Drawable?) : CustomImage(context) {
        init {
            drawable?.run {
                imageBm = toBitmap(
                    drawable.minimumWidth,
                    drawable.minimumHeight,
                    Bitmap.Config.ARGB_8888
                )
            }
        }
    }

    class Empty(context: Context) : CustomImage(context)
}

