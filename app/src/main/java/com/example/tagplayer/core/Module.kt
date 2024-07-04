package com.example.tagplayer.core

import androidx.lifecycle.ViewModel

interface Module<T : ViewModel> {
    fun create() : T
}