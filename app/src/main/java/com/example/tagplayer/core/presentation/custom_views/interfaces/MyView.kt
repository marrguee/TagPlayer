package com.example.tagplayer.core.presentation.custom_views.interfaces

interface MyView {
    fun title(title: String)
    fun color(resId: Int, color: String)
    fun image(bitmap: String?)
}