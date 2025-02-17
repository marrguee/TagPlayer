package com.example.tagplayer.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.HandleSaveRestoreState
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsScreen
import com.example.tagplayer.filter_by_tags.presentation.FilterTagsScreen
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.SortingType
import com.example.tagplayer.main.presentation.HandleSaveAndRestoreState
import com.example.tagplayer.main.presentation.NavigateEditSongTagScreen
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.recently.presentation.RecentlyScreen
import com.example.tagplayer.search.domain.SearchScreen
import com.example.tagplayer.tag_settings.presentation.TagSettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel(
    private val dispatcherList: DispatcherList,
    private val interactor: HomeInteractor,
    private val observable: CustomObservable.All<HomeState>,
    private val tagFiltersObservable: MutableStateFlow<TagFiltersState>,
    private val tagFilteredMapper: TagFilterMapper,
    private val songsResponseMapper: SongsResponse.SongsResponseMapper,
    private val navigation: Navigation.Navigate,
    private val handleDeath: HandleDeath
) : ViewModel(), StartPlayback, HandleUiStateUpdates.All<HomeState>,
    HandleSaveAndRestoreState<TagFiltersState>, NavigateEditSongTagScreen {

    private var sortingType: SortingType = SortingType.SortDateDesc
    private val sortingTypeMap = mapOf(
        Pair(0, SortingType.SortDateDesc),
        Pair(1, SortingType.SortDateAsc),
        Pair(2, SortingType.SortTitleAsc),
        Pair(3, SortingType.SortTitleDesc)
    )

    override fun init(
        bundle: HandleSaveRestoreState.Restore<TagFiltersState>
    ) {
        if (bundle.empty()) {
            viewModelScope.launch(dispatcherList.io()) {
                val filters: TagFiltersState = interactor.filters()
                tagFiltersObservable.emit(filters)
            }
            handleDeath.handleFirstStart()
        } else if (handleDeath.deathHappened()) {
            viewModelScope.launch {
                tagFiltersObservable.emit(bundle.restore())
                handleDeath.handleDeath()
            }
        }
    }

    override fun startGettingUpdates(observer: CustomObserver<HomeState>) {
        observable.updateObserver(observer)
        viewModelScope.launch(dispatcherList.io()) {
            interactor.croppedRecently().map(songsResponseMapper)
            withContext(dispatcherList.ui()) {
                tagFiltersObservable.collect {
                    it.map(tagFilteredMapper, sortingType)
                }
            }
        }
        interactor.scan()
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(HomeObserver.Empty)
    }

    override fun save(bundle: HandleSaveRestoreState.Save<TagFiltersState>) =
        bundle.save(tagFiltersObservable.value)

    override fun play(id: Long) = interactor.playSongForeground(id)

    override fun editSongTagsScreen(songId: Long) {
        navigation.update(EditSongTagsScreen(listOf(songId)))
    }

    fun sortSongs(option: Int) {
        sortingType = sortingTypeMap[option]?:SortingType.SortDateDesc
        tagFiltersObservable.value.map(tagFilteredMapper, sortingType)
    }

    fun filterTagsScreen() = navigation.update(FilterTagsScreen)
    fun recentlyPlayedScreen() = navigation.update(RecentlyScreen)
    fun tagSettingsScreen() = navigation.update(TagSettingsScreen)
    fun searchScreen() = navigation.update(SearchScreen)
    override fun clear() = observable.clear()

}