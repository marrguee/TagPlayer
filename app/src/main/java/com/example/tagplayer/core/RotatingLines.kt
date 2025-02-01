package com.example.tagplayer.core

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.animation.LinearInterpolator
import androidx.core.graphics.toRectF

class RotatingLines(private val invalidateFun: () -> Unit): HandleInternalDraw, HandleAnimationCycle {
    private val viewRect = Rect()
    private val smallerViewRect = Rect()
    private val externalPadding = 4
    private val internalPadding = 12
    private val externalArcAngle = 60f
    private val internalArcAngle = 170f

    private val paintBlack = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    private val paintGray = Paint().apply {
        color = Color.GRAY
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    private var blackRotation = 0f
    private var grayRotation = 0f

    private val blackAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration = 3000L
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            blackRotation = it.animatedValue as Float
            invalidateFun.invoke()
        }
    }

    private val grayAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration = 5000L
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            grayRotation = it.animatedValue as Float
            invalidateFun.invoke()
        }
    }

    fun onSizeChanged(newViewRect: Rect) {
        viewRect.set(newViewRect)
        viewRect.inset(externalPadding, externalPadding)
        smallerViewRect.set(viewRect)
        smallerViewRect.inset(internalPadding, internalPadding)
    }

    fun onDraw(canvas: Canvas, width: Int, height: Int) {
        val centerX = width / 2f
        val centerY = height / 2f

        canvas.save()
        canvas.rotate(blackRotation, centerX, centerY)
        canvas.drawArc(
            smallerViewRect.toRectF(),
            0f,
            internalArcAngle,
            false,
            paintGray
        )
        canvas.restore()

        canvas.save()
        canvas.rotate(grayRotation, centerX, centerY)
        canvas.drawArc(
            viewRect.toRectF(),
            0f,
            externalArcAngle,
            false,
            paintBlack
        )
        canvas.restore()
    }

    override fun provideRect(): Rect = Rect().apply {
        set(smallerViewRect)
        inset(internalPadding, internalPadding)
    }

    override fun startAnimation() {
        blackAnimator.start()
        grayAnimator.start()
    }

    override fun pauseAnimation() {
        blackAnimator.pause()
        grayAnimator.pause()
    }
}