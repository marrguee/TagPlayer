package com.example.tagplayer.core.presentation.custom_views.interfaces

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.graphics.toRectF
import com.example.tagplayer.core.domain.CustomImage

interface ImageFacade{
    fun changeImage(newImage: CustomImage)
    fun drawOnCanvas(canvas: Canvas, provideRect: HandleInternalDraw, src: Drawable)
    fun prepare(provideRect: HandleInternalDraw)

    class Base(private var image: CustomImage = CustomImage.Empty) : ImageFacade {
        private lateinit var maskBm: Bitmap
        private lateinit var resultBm: Bitmap
        private lateinit var srcBounds: Rect
        private lateinit var zoomedViewRect: Rect
        private var viewRect: Rect = Rect()
        private val maskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.FILL
        }

        override fun changeImage(newImage: CustomImage) {
            image = newImage
        }

        override fun drawOnCanvas(canvas: Canvas, provideRect: HandleInternalDraw, src: Drawable) {
            Canvas(maskBm).drawOval(viewRect.toRectF(), maskPaint)
            val resultCanvas = Canvas(resultBm)
            resultCanvas.drawBitmap(maskBm, viewRect, viewRect, null)
            image.drawOnCanvas(resultCanvas, zoomedViewRect)
            canvas.drawBitmap(resultBm, viewRect, provideRect.provideRect(), null)
            src.bounds = srcBounds
            src.draw(canvas)
        }

        override fun prepare(provideRect: HandleInternalDraw) {
            val providedRect = provideRect.provideRect()
            if (viewRect == providedRect) return
            viewRect = Rect(
                0,
                0,
                providedRect.width(),
                providedRect.height()
            )
            maskBm = Bitmap.createBitmap(
                viewRect.width(),
                viewRect.height(),
                Bitmap.Config.ALPHA_8
            )
            resultBm = maskBm.copy(Bitmap.Config.ARGB_8888, true)

            val halfHeight = (providedRect.height() / 2 * SCALE).toInt()
            val halfWidth = (providedRect.width() / 2 * SCALE).toInt()
            srcBounds = Rect().apply {
                set(providedRect)
                inset(halfHeight, halfWidth)
            }

            val zoomStep = (viewRect.width()* ZOOM).toInt()
            zoomedViewRect = Rect(
                viewRect.left - zoomStep,
                viewRect.top - zoomStep,
                viewRect.right + zoomStep,
                viewRect.bottom + zoomStep
            )
        }

        companion object {
            private const val ZOOM = 0.1f
            private const val SCALE = ZOOM * 4
        }
    }

}

