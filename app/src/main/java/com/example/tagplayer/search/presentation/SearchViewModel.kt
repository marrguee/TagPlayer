package com.example.tagplayer.search.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.PlayForeground
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.viewmodel.ComebackViewModel
import com.example.tagplayer.core.presentation.viewmodel.NavigateAttachTagsScreen
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse
import com.example.tagplayer.tags_attach.presentation.AttachTagsScreen

class SearchViewModel(
    private val runAsync: RunAsync,
    private val interactor: SearchInteractor,
    private val observable: CustomObservable.All<SearchState>,
    private val mapper: SearchResponse.Mapper,
    private val navigation: Navigation.Navigate,
) : ComebackViewModel(), PlayForeground, HandleUiStateUpdates.All<SearchState>,
    NavigateAttachTagsScreen {

    fun search(query: String) = runAsync.handle(viewModelScope, { it.map(mapper) }) {
        interactor.search(query)
    }

    override fun play(id: Long) = interactor.play(id)

    override fun attachTagsScreen(songId: Long) = navigation.update(AttachTagsScreen(songId))

    override fun startGettingUpdates(observer: CustomObserver<SearchState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(SearchObserver.Empty)

    override fun clear() = observable.clear()

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }
}