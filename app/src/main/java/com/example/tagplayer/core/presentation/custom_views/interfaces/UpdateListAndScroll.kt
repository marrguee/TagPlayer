package com.example.tagplayer.core.presentation.custom_views.interfaces

interface UpdateListAndScroll<T> {
    fun updateAndScrollToFirst(list: List<T>)
    fun firstItemVisible(): Boolean
}
