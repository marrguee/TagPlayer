package com.example.tagplayer.core.domain

import androidx.lifecycle.ViewModel

interface ClearViewModel {
    fun clear(clazz: Class<out ViewModel>)
}