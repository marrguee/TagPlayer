package com.example.tagplayer.main.presentation

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.domain.ClearViewModel

abstract class ComebackViewModelsModule(
    private val clearBlock: () -> Unit
) : ComebackViewModel(object : ClearViewModel {
        override fun clear(clazz: Class<out ViewModel>) {
            clearBlock.invoke()
        }
    })