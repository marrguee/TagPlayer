package com.example.tagplayer.core

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import kotlin.math.abs

abstract class CustomImage: Parcelable {
    private var imageBmBounds: Rect = Rect()
    private val imageBmPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
    protected var imageBm: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)
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

            imageBmBounds = Rect(
                0, 0,
                field.width, field.height
            )
        }

    fun drawOnCanvas(canvas: Canvas, zoomedViewRect: Rect) {
        canvas.drawBitmap(imageBm, imageBmBounds, zoomedViewRect, imageBmPaint)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        val byteArray = bitmapToByteArray(imageBm)
        parcel.writeByteArray(byteArray)
    }

    override fun describeContents(): Int = 0

    companion object {
        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            return ByteArrayOutputStream().use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.toByteArray()
            }
        }

        fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    @SuppressLint("ParcelCreator")
    class ByteArrayVariant(byteArray: ByteArray) :
        CustomImage() {
        init {
            imageBm = byteArrayToBitmap(byteArray)
        }

        @JvmField
        val CREATOR: Parcelable.Creator<ByteArrayVariant> = object : Parcelable.Creator<ByteArrayVariant> {
            override fun createFromParcel(parcel: Parcel): ByteArrayVariant {
                val parcelByteArray = parcel.createByteArray() ?: byteArrayOf()
                return ByteArrayVariant(parcelByteArray)
            }

            override fun newArray(size: Int): Array<ByteArrayVariant?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
    class DrawableVariant(drawable: Drawable) :
        CustomImage() {
        init {
            drawable.let {
                imageBm = it.toBitmap(
                    it.minimumWidth,
                    it.minimumHeight,
                    Bitmap.Config.ARGB_8888
                )
            }
        }
        @JvmField
        val CREATOR: Parcelable.Creator<DrawableVariant> = object : Parcelable.Creator<DrawableVariant> {
            override fun createFromParcel(parcel: Parcel): DrawableVariant {
                val byteArray = parcel.createByteArray() ?: byteArrayOf()
                val bitmap = byteArrayToBitmap(byteArray)
                return DrawableVariant(BitmapDrawable(null, bitmap))
            }

            override fun newArray(size: Int): Array<DrawableVariant?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
    object Empty : CustomImage() {
        @JvmField
        val CREATOR: Parcelable.Creator<Empty> = object : Parcelable.Creator<Empty> {
            override fun createFromParcel(parcel: Parcel): Empty = Empty
            override fun newArray(size: Int): Array<Empty?> = arrayOfNulls(size)
        }
    }
}