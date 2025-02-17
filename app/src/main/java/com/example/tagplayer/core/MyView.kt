package com.example.tagplayer.core

interface MyView {
    fun title(title: String)
    fun color(resId: Int, color: String)
    fun image(bitmap: String?)
}