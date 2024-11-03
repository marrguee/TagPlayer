package com.example.tagplayer.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.tagplayer.R
import kotlin.math.abs

abstract class CustomImage(context: Context) {
    protected var imageBm: Bitmap
    private val imageBmBounds: Rect
    private val imageBmPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    init {
        imageBm = ContextCompat.getDrawable(
            context,
            R.drawable.placeholder_music
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