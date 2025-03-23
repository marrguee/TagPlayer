package com.example.tagplayer.core.presentation.custom_views.interfaces

interface UpdateList<T> {
    fun update(list: List<T>, block: () -> Unit)
}