package com.example.tagplayer.recently.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.tags_attach.presentation.AttachTagsScreen
import com.example.tagplayer.core.presentation.viewmodel.ComebackViewModel
import com.example.tagplayer.core.presentation.viewmodel.NavigateAttachTagsScreen
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.recently.domain.RecentlyInteractor
import com.example.tagplayer.recently.domain.RecentlyResponse

class RecentlyViewModel(
    private val runAsync: RunAsync,
    private val interactor: RecentlyInteractor,
    private val observable: CustomObservable.All<RecentlyState>,
    private val mapper: RecentlyResponse.HistoryResponseMapper,
    private val navigation: Navigation.Navigate,
    private val handleDeath: HandleDeath,
    clear: ClearViewModel
) : ComebackViewModel(clear), HandleUiStateUpdates.All<RecentlyState>, StartPlayback,
    NavigateAttachTagsScreen {

    fun init() {
        if (handleDeath.deathHappened()) {
            runAsync.handle(viewModelScope, { it.map(mapper) }) { interactor.recently() }
            handleDeath.handleDeath()
        }
    }

    override fun play(id: Long) = interactor.play(id)

    override fun startGettingUpdates(observer: CustomObserver<RecentlyState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(RecentlyObserver.Empty)

    override fun clear() = observable.clear()

    override fun attachTagsScreen(songId: Long) = navigation.update(AttachTagsScreen(songId))

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }
}

