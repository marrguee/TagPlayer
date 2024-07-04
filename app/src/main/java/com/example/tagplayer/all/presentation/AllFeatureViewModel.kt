package com.example.tagplayer.all.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.all.domain.AllInteractor
import com.example.tagplayer.all.domain.AllResponse
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.StartPlayback

class AllFeatureViewModel(
    private val interactor: AllInteractor,
    private val communication: Communication<AllState>,
    private val mapper: AllResponse.AllResponseMapper
) : ViewModel(), StartPlayback {

    fun loadTracks() =
        interactor.tracksFlow().map(mapper, viewModelScope)

    fun scan() = interactor.scan()

    fun observe(owner: LifecycleOwner, observer: Observer<in AllState>) =
        communication.observe(owner, observer)


    override fun play(id: Long) =
        interactor.playSongForeground(id)
}