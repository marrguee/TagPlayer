package com.example.tagplayer.history.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.data.LastPlayed
import com.example.tagplayer.history.domain.SongHistoryInteractor
import com.example.tagplayer.history.domain.HistoryResponse
import com.example.tagplayer.core.domain.StartPlayback
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class HistoryViewModel(
    private val communication: Communication<HistoryState>,
    private val interactor: SongHistoryInteractor,
    private val mapper: HistoryResponse.HistoryResponseMapper
) : ViewModel(), StartPlayback {
    fun history() {
        interactor.playedHistory().map(mapper, viewModelScope)
    }

    override fun play(id: Long) {
        val now: Date = Calendar.getInstance().time

        viewModelScope.launch {
            interactor.songToHistory(LastPlayed(id, now))
        }

        interactor.playSongForeground(id)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in HistoryState>) {
        communication.observe(owner, observer)
    }
}

