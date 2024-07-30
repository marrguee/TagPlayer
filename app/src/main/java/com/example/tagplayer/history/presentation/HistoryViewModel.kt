package com.example.tagplayer.history.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.history.domain.SongHistoryInteractor
import com.example.tagplayer.history.domain.HistoryResponse
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.main.Navigation
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class HistoryViewModel(
    private val interactor: SongHistoryInteractor,
    private val communication: Communication<HistoryState>,
    private val mapper: HistoryResponse.HistoryResponseMapper
) : ViewModel(), StartPlayback {
    fun history() {
        interactor.playedHistory().map(mapper, viewModelScope)
    }

    override fun play(id: Long) {
        //val now: Date = Calendar.getInstance().time
        interactor.playSongForeground(id)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in HistoryState>) {
        communication.observe(owner, observer)
    }
}

