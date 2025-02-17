package com.example.tagplayer.main.presentation

import androidx.viewbinding.ViewBinding

abstract class ArgumentsComebackFragment<B : ViewBinding, V : ComebackViewModel>(
    protected val keyList: List<String>
) : ComebackFragment<B, V>() {
        fun provideKeyList() = keyList.toList()
}