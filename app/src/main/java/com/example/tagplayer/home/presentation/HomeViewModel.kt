package com.example.tagplayer.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.HandleSaveRestoreState
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsScreen
import com.example.tagplayer.filter_by_tags.presentation.FilterTagsScreen
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.main.presentation.HandleSaveAndRestoreState
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.recently.presentation.RecentlyScreen
import com.example.tagplayer.search.domain.SearchScreen
import com.example.tagplayer.tag_settings.presentation.TagSettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel(
    private val interactor: HomeInteractor,
    private val observable: CustomObservable.All<HomeState>,
    private val tagFiltersObservable: CustomObservable.AllHandleState<TagFiltersState>,
    private val tagFilteredMapper: TagFilterMapper,
    private val navigation: Navigation.Navigate,
    private val handleDeath: HandleDeath,
    private val selectedSongId: AtomicLong
) : ViewModel(), StartPlayback, HandleUiStateUpdates.All<HomeState>,
    HandleSaveAndRestoreState<TagFiltersState> {

    override fun init(
        bundle: HandleSaveRestoreState.Restore<TagFiltersState>
    ) {
        if (bundle.empty()) {
            Log.d("HomeViewModel: ", "bundle.empty()")
            viewModelScope.launch(Dispatchers.IO) {
                val filters = interactor.filters()
                tagFiltersObservable.update(filters)
            }
            handleDeath.handleFirstStart()
        } else if (handleDeath.deathHappened()) {
            Log.d("HomeViewModel: ", "deathHappened")
            tagFiltersObservable.restore(bundle)
            handleDeath.handleDeath()
        }
    }

    override fun startGettingUpdates(observer: CustomObserver<HomeState>) {
        observable.updateObserver(observer)
        tagFiltersObservable.updateObserver(object : CustomObserver<TagFiltersState> {
            override fun update(data: TagFiltersState) {
                Log.d("HomeViewModel: ", "data.map(tagFilteredMapper)")
                data.map(tagFilteredMapper)
            }
        })
        interactor.scan()
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(HomeObserver.Empty)
        tagFiltersObservable.updateObserver(TagFiltersObserver.Empty)
    }

    override fun save(
        bundle: HandleSaveRestoreState.Save<TagFiltersState>
    ) {
        tagFiltersObservable.save(bundle)
    }

    override fun play(id: Long) =
        interactor.playSongForeground(id)

    fun filterTagsScreen() = navigation.update(FilterTagsScreen)

    fun editSongTagsScreen(songId: Long) {
        selectedSongId.set(songId)
        navigation.update(EditSongTagsScreen)
    }
    fun recentlyPlayedScreen() = navigation.update(RecentlyScreen)
    fun tagSettingsScreen() = navigation.update(TagSettingsScreen)
    fun searchScreen() = navigation.update(SearchScreen)
    override fun clear() = observable.clear()

}