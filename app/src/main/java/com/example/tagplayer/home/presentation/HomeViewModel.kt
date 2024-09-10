package com.example.tagplayer.home.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.HomeResponse
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.edit_song_tag.EditSongTagsScreen
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.recently.presentation.RecentlyScreen
import com.example.tagplayer.tagsettings.presentation.TagSettingsScreen
import kotlinx.coroutines.launch

class HomeViewModel(
    private val interactor: HomeInteractor,
    private val communication: Communication<HomeState>,
    private val mapper: HomeResponse.HomeResponseMapper,
    private val navigation: Navigation.Navigate
) : ViewModel(), StartPlayback {

    fun editSongTagsScreen(songId: Long) {
        navigation.update(EditSongTagsScreen(songId))
    }

    fun loadLibrary() {
        viewModelScope.launch {
            interactor.libraryFlow().collect {
                communication.update(HomeState.LibraryUpdated(it))
            }
        }
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