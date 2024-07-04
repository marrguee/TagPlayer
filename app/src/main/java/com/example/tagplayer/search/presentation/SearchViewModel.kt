package com.example.tagplayer.search.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse
import com.example.tagplayer.search.domain.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class SearchViewModel(
    private val interactor: SearchInteractor,
    private val communication: Communication<SearchState>,
    private val searchResponseMapper: SearchResponse.SearchResponseMapper,
    private val dispatcherList: DispatcherList,
    private val clear: () -> Unit
) : ViewModel(), PlaySongForeground {

    fun finish() {
        clear.invoke()
    }

    fun searchHistory() {
        viewModelScope.launch(dispatcherList.io()) {
            val result = interactor.searchHistory()
            withContext(dispatcherList.ui()){
                result.map(searchResponseMapper, viewModelScope)
            }
        }
    }

    fun updateHistory(searchRequest: String) {
        viewModelScope.launch(dispatcherList.io()) {
            interactor.updateSearch(SearchHistoryTable(searchRequest, Date()))
        }
    }

    fun findSongs(query: String) =
        interactor.findSongs(query).map(searchResponseMapper, viewModelScope)

    fun observe(owner: LifecycleOwner, observer: Observer<in SearchState>) =
        communication.observe(owner, observer)

    override fun playSongForeground(id: Long) =
        interactor.playSongForeground(id)
}