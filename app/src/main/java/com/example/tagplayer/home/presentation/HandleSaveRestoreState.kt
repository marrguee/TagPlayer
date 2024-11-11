package com.example.tagplayer.home.presentation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

interface HandleSaveRestoreState<T> {
    fun save(data: T)
    fun restore(): T
    fun empty(): Boolean

    abstract class Base<T : Parcelable>(
        private val bundle: Bundle?,
        private val key: String = "DefaultKey"
    ): HandleSaveRestoreState<T> {
        override fun empty(): Boolean = bundle == null

        override fun save(data: T) {
            bundle?.putParcelable(key, data)
        }

        override fun restore(): T {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getParcelable(key, Any::class.java)!!
            } else {
                bundle?.getParcelable(key)!!
            }  as T
        }
    }
}