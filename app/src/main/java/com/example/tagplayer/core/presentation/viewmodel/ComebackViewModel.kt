package com.example.tagplayer.core.presentation.viewmodel

import androidx.lifecycle.ViewModel

abstract class ComebackViewModel : ViewModel(), HandleComeback {
    override fun comeback() = Unit
}