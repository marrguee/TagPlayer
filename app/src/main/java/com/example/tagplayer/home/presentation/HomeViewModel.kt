package com.example.tagplayer.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.tags_attach.presentation.AttachTagsScreen
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.domain.StartPlayback
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.viewmodel.NavigateAttachTagsScreen
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.filter.presentation.FilterTagsScreen
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.SongsResponse
import com.example.tagplayer.home.domain.SortType
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.recently.presentation.RecentlyScreen
import com.example.tagplayer.search.domain.SearchScreen
import com.example.tagplayer.tag_settings.presentation.TagSettingsScreen

class HomeViewModel(
    private val runAsync: RunAsync,
    private val interactor: HomeInteractor,
    private val observable: CustomObservable.All<HomeState>,
    private val mapper: SongsResponse.Mapper,
    private val navigation: Navigation.Navigate,
    private val handleDecline: HandleDeclineText.ProvideHandleDeclineText
) : ViewModel(), StartPlayback, HandleUiStateUpdates.All<HomeState>,
    NavigateAttachTagsScreen, SortSongs {
    private val uiBlock: (SongsResponse) -> Unit = { it.map(mapper, viewModelScope) }

    fun scan() = interactor.scan()

    fun loadRecently() = runAsync.handle(viewModelScope, uiBlock) {
        interactor.croppedRecently()
    }

    fun handlePermission(
        permission: String
    ) {
        observable.update(
            HomeState.ShowAlertPermissions(handleDecline.permissionTextProvider(permission))
        )
    }

    override fun sort(type: SortType.Map) = runAsync.handle(viewModelScope, uiBlock) {
         interactor.sortedSongs(type)
    }

    override fun startGettingUpdates(observer: CustomObserver<HomeState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(HomeObserver.Empty)
    override fun play(id: Long) = interactor.play(id)
    override fun attachTagsScreen(songId: Long) = navigation.update(AttachTagsScreen(songId))
    override fun clear() = observable.clear()

    fun filterTagsScreen() = navigation.update(FilterTagsScreen)
    fun recentlyPlayedScreen() = navigation.update(RecentlyScreen)
    fun tagSettingsScreen() = navigation.update(TagSettingsScreen)
    fun searchScreen() = navigation.update(SearchScreen)
}