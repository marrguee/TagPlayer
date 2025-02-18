package com.example.tagplayer.core.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.abs

interface CustomImage {
    fun drawOnCanvas(canvas: Canvas, zoomedViewRect: Rect)

    class Builder {
        fun create(array: ByteArray?) = if (array == null) Empty else ByteImage(array)
    }

    abstract class BaseImage : CustomImage {
        private var bounds: Rect = Rect()
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
        protected var image: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)
            set(value) {
                field = value

                if (value.width != value.height) {
                    val indent = abs(value.width - value.height) / 2

                    field = if (value.width > value.height) {
                        Bitmap.createBitmap(value, indent, 0, value.height, value.height)
                    } else {
                        Bitmap.createBitmap(value, 0, indent, value.width, value.width)
                    }
                }

                bounds = Rect(
                    0, 0,
                    field.width, field.height
                )
            }

        override fun drawOnCanvas(canvas: Canvas, zoomedViewRect: Rect) {
            canvas.drawBitmap(image, bounds, zoomedViewRect, paint)
        }
    }

    class ByteImage(array: ByteArray) : BaseImage() {
        init { image = BitmapFactory.decodeByteArray(array, 0, array.size) }
    }

    class DrawableImage(drawable: Drawable) : BaseImage() {
        init {
            image = drawable.run { toBitmap(minimumWidth, minimumHeight, Bitmap.Config.ARGB_8888) }
        }
    }

    object Empty : BaseImage()
}
