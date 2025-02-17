package com.example.tagplayer.core

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

interface HandleSaveRestoreState {
    interface Save<T> {
        fun save(data: T)
    }
    interface Restore<T> {
        fun restore(): T
        fun empty(): Boolean
    }
    interface All<T> : Save<T>, Restore<T>

    abstract class Base<T : Parcelable>(
        private val bundle: Bundle?,
        private val key: String = "DefaultKey"
    ): All<T> {
        override fun empty(): Boolean = bundle == null

        override fun save(data: T) {
            bundle?.putParcelable(key, data)
        }

        override fun restore(): T {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getParcelable(key, Any::class.java)!!
            } else {
                bundle?.getParcelable(key)!!
            } as T
        }
    }
}