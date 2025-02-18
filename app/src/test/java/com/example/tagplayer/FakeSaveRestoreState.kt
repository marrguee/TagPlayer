package com.example.tagplayer

import android.os.Bundle
import com.example.tagplayer.core.presentation.HandleSaveRestoreState

interface FakeSaveRestoreState<T>: HandleSaveRestoreState.All<T> {
    abstract class Base<T>(
        empty: T,
        private val bundle: Bundle?,
    ): FakeSaveRestoreState<T> {
        private var state: T = empty

        override fun save(data: T) {
            state = data
        }

        override fun restore(): T = state

        override fun empty(): Boolean = bundle == null

    }
}