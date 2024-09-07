package com.example.tagplayer.recently.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.recently.domain.RecentlyInteractor
import com.example.tagplayer.recently.domain.RecentlyResponse
import com.example.tagplayer.core.domain.StartPlayback
import kotlinx.coroutines.launch

class RecentlyViewModel(
    private val interactor: RecentlyInteractor,
    private val communication: Communication<RecentlyState>,
    private val mapper: RecentlyResponse.HistoryResponseMapper
) : ViewModel(), StartPlayback {
    fun recently() {
        viewModelScope.launch {
            interactor.recently().map(mapper)
        }
    }

    override fun play(id: Long) {
        //val now: Date = Calendar.getInstance().time
        interactor.playSongForeground(id)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in RecentlyState>) {
        communication.observe(owner, observer)
    }
}

