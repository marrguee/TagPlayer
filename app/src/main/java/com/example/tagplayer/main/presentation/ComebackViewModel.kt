package com.example.tagplayer.main.presentation

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.domain.ClearViewModel

abstract class ComebackViewModel(
    private val clear: ClearViewModel
) : ViewModel(), HandleComeback {

    override fun comeback() {
        clear.clear(this::class.java)
    }
}