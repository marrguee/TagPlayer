package com.example.tagplayer.home.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.HomeResponse
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.edit_song_tag.EditSongTagsScreen
import com.example.tagplayer.filter_by_tags.FilterTagsScreen
import com.example.tagplayer.filter_by_tags.TagFilterUi
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.recently.presentation.RecentlyScreen
import com.example.tagplayer.tagsettings.presentation.TagSettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val interactor: HomeInteractor,
    private val communication: Communication<HomeState>,
    private val tagFilterCommunication: CustomObservable.All<List<Long>>,
    private val mapper: HomeResponse.HomeResponseMapper,
    private val navigation: Navigation.Navigate
) : ViewModel(), StartPlayback {
    private var flowJob: Job? = null

    fun init() {

        viewModelScope.launch {
            val filters = interactor.filters()
            tagFilterCommunication.update(filters)
            withContext(Dispatchers.Main.immediate) {
                tagFilterCommunication.updateObserver(object : CustomObserver<List<Long>> {
                    override fun update(data: List<Long>) {
                        viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
                            if (data.isNotEmpty()) {
                                flowJob?.let {
                                    cancel()
                                    flowJob = null
                                }
                                val filteredSongs = interactor.filtered(data)
                                withContext(Dispatchers.Main.immediate) {
                                    communication.update(HomeState.LibraryUpdated(filteredSongs))
                                }
                            } else {
                                if (flowJob == null)
                                    flowJob = viewModelScope.launch {
                                        val list = interactor.libraryFlow()
                                        withContext(Dispatchers.Main.immediate) {
                                            list.collect {
                                                communication.update(HomeState.LibraryUpdated(it))
                                            }
                                        }
                                    }
                            }
                        }
                    }
                })
            }
        }

    }

    fun stop() {
        //tagFilterCommunication.clear()
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

    fun loadRecently() {
        viewModelScope.launch {
            interactor.recently().map(mapper)
        }
    }

    fun scan() = interactor.scan()

    fun observe(owner: LifecycleOwner, observer: Observer<in HomeState>) =
        communication.observe(owner, observer)


    override fun play(id: Long) =
        interactor.playSongForeground(id)
}