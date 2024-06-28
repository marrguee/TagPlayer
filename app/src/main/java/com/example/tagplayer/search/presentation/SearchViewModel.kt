package com.example.tagplayer.search.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.data.LastPlayed
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.search.data.SearchHistory
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
    private val searchResponseMapper: SearchResponse.SearchResponseMapper
) : ViewModel(), PlaySongForeground {
    fun searchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.searchHistory()
            withContext(Dispatchers.Main.immediate){ result.map(searchResponseMapper) }
        }
    }

    fun updateHistory(searchRequest: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.updateSearch(SearchHistory(searchRequest, Date()))
        }
    }

    fun findSongs(query: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.findSongs(query)
            withContext(Dispatchers.Main.immediate) {
                result.map(searchResponseMapper)
            }
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in SearchState>) {
        communication.observe(owner, observer)
    }

    override fun playSongForeground(id: Long) {
        viewModelScope.launch {
            interactor.songToHistory(
                LastPlayed(id, Calendar.getInstance().time)
            )
        }
        interactor.playSongForeground(id)
    }
}