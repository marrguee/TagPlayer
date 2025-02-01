package com.example.tagplayer.core

import android.graphics.Rect

interface HandleInternalDraw {
    fun provideRect() : Rect
}