package com.example.tagplayer.all.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.all.domain.AllInteractor
import com.example.tagplayer.all.domain.AllResponse
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.RunAsync
import com.example.tagplayer.core.data.LastPlayed
import com.example.tagplayer.core.domain.StartPlayback
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class AllFeatureViewModel(
    private val runAsync: RunAsync,
    private val interactor: AllInteractor,
    private val communication: Communication<AllState>,
    private val mapper: AllResponse.AllResponseMapper
) : ViewModel(), StartPlayback {

    fun loadTracks(){
        interactor.tracksFlow().map(mapper, viewModelScope)
    }

    fun scan() {
        interactor.searchSongsForeground()
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in AllState>) {
        communication.observe(owner, observer)
    }


    override fun play(id: Long){

        viewModelScope.launch {
            interactor.songToHistory(
                LastPlayed(id, Calendar.getInstance().time)
            )
        }
        interactor.playSongForeground(id)
    }
}