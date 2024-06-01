package com.example.tagplayer.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.tagplayer.all.presentation.AllState

interface Communication<T> {
    fun update(data: T)
    fun observe(owner: LifecycleOwner, observer: Observer<in T>)

    class Base : Communication<AllState> {
        private val liveData = MutableLiveData<AllState>()
        override fun update(data: AllState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in AllState>) {
            liveData.observe(owner, observer)
        }
    }
}