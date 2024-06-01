package com.example.tagplayer.all.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.all.domain.Interactor
import com.example.tagplayer.all.domain.Response
import com.example.tagplayer.core.Communication
import com.example.tagplayer.core.RunAsync

class AllFeatureViewModel(
    private val runAsync: RunAsync,
    private val interactor: Interactor,
    private val communication: Communication<AllState>,
    private val mapper: Response.Mapper
) : ViewModel() {

    fun loadTracks(){
        runAsync.run(
            viewModelScope,
            { interactor.tracks() }
        ) {
            it.map(mapper)
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in AllState>) {
        communication.observe(owner, observer)
    }
}