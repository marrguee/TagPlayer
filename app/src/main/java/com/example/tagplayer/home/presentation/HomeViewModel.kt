package com.example.tagplayer.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.edit_song_tag.EditSongTagsScreen
import com.example.tagplayer.filter_by_tags.FilterTagsScreen
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.HomeResponse
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.recently.presentation.RecentlyScreen
import com.example.tagplayer.search.domain.SearchScreen
import com.example.tagplayer.tagsettings.presentation.TagSettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val interactor: HomeInteractor,
    private val observable: CustomObservable.All<HomeState>,
    private val tagFilterCommunication: CustomObservable.Mutable<List<Long>>,
    private val mapper: HomeResponse.HomeResponseMapper,
    private val navigation: Navigation.Navigate
) : ViewModel(), StartPlayback, HandleUiStateUpdates.All<HomeState> {
    private var flowJob: Job? = null

    fun init() {
        interactor.scan()
    }

    override fun startGettingUpdates(observer: CustomObserver<HomeState>) {
        observable.updateObserver(observer)
    }

    fun startGettingFilterUpdates() {
        viewModelScope.launch {
            val filters = interactor.filters()
            tagFilterCommunication.update(filters)
            withContext(Dispatchers.Main.immediate) {
                tagFilterCommunication.updateObserver(object : CustomObserver<List<Long>> {
                    override fun update(data: List<Long>) {
                        if (data.isNotEmpty()) {
                            viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
                                flowJob?.let {
                                    cancel()
                                    flowJob = null
                                }
                                val filteredSongs = interactor.filtered(data)
                                withContext(Dispatchers.Main.immediate) {
                                    observable.update(HomeState.LibraryUpdated(filteredSongs))
                                }
                            }
                        } else {
                            if (flowJob == null)
                                flowJob = viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
                                    val list = interactor.libraryFlow()
                                    withContext(Dispatchers.Main.immediate) {
                                        list.collect {
                                            observable.update(HomeState.LibraryUpdated(it))
                                        }
                                    }
                                }
                        }
                    }
                })
            }
        }
    }


    fun loadRecently() {
        viewModelScope.launch {
            interactor.recently().map(mapper)
        }
    }

    override fun play(id: Long) =
        interactor.playSongForeground(id)

    override fun stopGettingUpdates() {
        observable.updateObserver(HomeObserver.Empty)
    }

    override fun clear() {
        observable.clear()
    }

    fun filterTagsScreen() {
        navigation.update(FilterTagsScreen)
    }

    fun editSongTagsScreen(songId: Long) {
        navigation.update(EditSongTagsScreen(songId))
    }

    fun recentlyPlayedScreen() {
        navigation.update(RecentlyScreen)
    }

    fun tagSettingsScreen() {
        navigation.update(TagSettingsScreen)
    }

    fun searchScreen() {
        navigation.update(SearchScreen)
    }
}