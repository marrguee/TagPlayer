package com.example.tagplayer.recently.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.HandleSaveRestoreState
import com.example.tagplayer.main.presentation.ComebackViewModel
import com.example.tagplayer.main.presentation.HandleSaveAndRestoreState
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import com.example.tagplayer.recently.domain.RecentlyInteractor
import com.example.tagplayer.recently.domain.RecentlyResponse
import kotlinx.coroutines.launch

class RecentlyViewModel(
    private val interactor: RecentlyInteractor,
    private val observable: CustomObservable.AllHandleState<RecentlyState>,
    private val mapper: RecentlyResponse.HistoryResponseMapper,
    private val navigation: Navigation.Navigate,
    private val handleDeath: HandleDeath,
    clear: ClearViewModel
) : ComebackViewModel(clear), HandleUiStateUpdates.All<RecentlyState>, StartPlayback,
    HandleSaveAndRestoreState<RecentlyState> {

    override fun play(id: Long) {
        interactor.playSongForeground(id)
    }

    override fun startGettingUpdates(observer: CustomObserver<RecentlyState>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(RecentlyObserver.Empty)
    }

    override fun clear() {
        observable.clear()
    }

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }

    override fun init(bundle: HandleSaveRestoreState.Restore<RecentlyState>) {
        if (bundle.empty()) {
            viewModelScope.launch {
                interactor.recently().map(mapper)
            }
            handleDeath.handleFirstStart()
        } else if (handleDeath.deathHappened()) {
            observable.restore(bundle)
            handleDeath.handleDeath()
        }
    }

    override fun save(bundle: HandleSaveRestoreState.Save<RecentlyState>) {
        observable.save(bundle)
    }
}

