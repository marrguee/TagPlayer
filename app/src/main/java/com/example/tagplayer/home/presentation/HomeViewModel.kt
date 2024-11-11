package com.example.tagplayer.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.edit_song_tag.presentation.EditSongTagsScreen
import com.example.tagplayer.filter_by_tags.presentation.FilterTagsScreen
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.recently.presentation.RecentlyScreen
import com.example.tagplayer.search.domain.SearchScreen
import com.example.tagplayer.tagsettings.presentation.TagSettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val interactor: HomeInteractor,
    private val observable: CustomObservable.All<HomeState>,
    private val tagFiltersObservable: CustomObservable.Mutable<TagFiltersResponse>,
    private val tagFilteredMapper: TagFilterMapper,
    private val navigation: Navigation.Navigate,
    private val handleDeath: HandelDeath,
) : ViewModel(), StartPlayback, HandleUiStateUpdates.All<HomeState> {
    private var tagFiltersResponse: TagFiltersResponse = TagFiltersResponse.Empty

    fun init(bundle: HandleSaveRestoreState<TagFiltersResponse>) {
        if (bundle.empty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val filters = interactor.filters()
                tagFiltersResponse = filters
                tagFiltersObservable.update(filters)
            }
            handleDeath.handleDeath()
        } else if (handleDeath.deathHappened()) {
            tagFiltersResponse = bundle.restore()
            tagFiltersObservable.update(tagFiltersResponse)
            handleDeath.handleDeath()
        }
    }

    override fun startGettingUpdates(observer: CustomObserver<HomeState>) {
        observable.updateObserver(observer)
        tagFiltersObservable.updateObserver(object : CustomObserver<TagFiltersResponse> {
            override fun update(data: TagFiltersResponse) {
                tagFiltersResponse = data
                tagFiltersResponse.map(tagFilteredMapper)
            }
        })
        interactor.scan()
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(HomeObserver.Empty)
        tagFiltersObservable.updateObserver(TagFiltersObserver.Empty)
    }

    override fun play(id: Long) =
        interactor.playSongForeground(id)

    fun filterTagsScreen() {
        tagFiltersObservable.updateObserver(TagFiltersObserver.Empty)
        tagFiltersObservable.update(tagFiltersResponse)
        navigation.update(FilterTagsScreen)
    }

    fun editSongTagsScreen(songId: Long) = navigation.update(EditSongTagsScreen(songId))
    fun recentlyPlayedScreen() = navigation.update(RecentlyScreen)
    fun tagSettingsScreen() = navigation.update(TagSettingsScreen)
    fun searchScreen() = navigation.update(SearchScreen)
    override fun clear() = observable.clear()
}