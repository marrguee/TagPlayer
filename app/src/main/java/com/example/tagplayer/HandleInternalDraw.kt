package com.example.tagplayer

import android.graphics.Rect

interface HandleInternalDraw {
    fun provideRect() : Rect
}