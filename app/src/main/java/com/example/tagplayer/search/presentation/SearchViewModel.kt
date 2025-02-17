package com.example.tagplayer.search.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsScreen
import com.example.tagplayer.main.presentation.ComebackViewModel
import com.example.tagplayer.main.presentation.NavigateEditSongTagScreen
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse
import com.example.tagplayer.search.domain.SearchState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    clear: ClearViewModel,
    private val dispatcherList: DispatcherList,
    private val interactor: SearchInteractor,
    private val observable: CustomObservable.All<SearchState>,
    private val searchResponseMapper: SearchResponse.SearchResponseMapper,
    private val navigation: Navigation.Navigate,
) : ComebackViewModel(clear), PlaySongForeground, HandleUiStateUpdates.All<SearchState>,
    NavigateEditSongTagScreen {

    fun findSongs(query: String) {
        viewModelScope.launch(dispatcherList.io()) {
            val response = interactor.findSongsByTitle(query)
            withContext(dispatcherList.ui()){
                response.map(searchResponseMapper)
            }
        }
    }

    override fun playSongForeground(id: Long) =
        interactor.playSongForeground(id)

    override fun editSongTagsScreen(songId: Long) {
        navigation.update(EditSongTagsScreen(listOf(songId)))
    }

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