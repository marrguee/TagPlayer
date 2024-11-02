package com.example.tagplayer.search.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.main.presentation.ComebackViewModel
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse
import com.example.tagplayer.search.domain.SearchState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val interactor: SearchInteractor,
    private val observable: CustomObservable.All<SearchState>,
    private val searchResponseMapper: SearchResponse.SearchResponseMapper,
    private val dispatcherList: DispatcherList,
    private val navigation: Navigation.Navigate,
    clear: ClearViewModel
) : ComebackViewModel(clear), PlaySongForeground, HandleUiStateUpdates.All<SearchState> {

    fun findSongs(query: String) {
        viewModelScope.launch(dispatcherList.io()) {
            val result: SearchResponse = interactor.findSongsByTitle(query)
            withContext(dispatcherList.ui()){
                result.map(searchResponseMapper)
            }
        }
    }

    override fun playSongForeground(id: Long) =
        interactor.playSongForeground(id)

    override fun startGettingUpdates(observer: CustomObserver<SearchState>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(SearchObserver.Empty)
    }

    override fun clear() {
        observable.clear()
    }

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }
}