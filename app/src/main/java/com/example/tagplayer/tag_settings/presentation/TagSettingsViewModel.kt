package com.example.tagplayer.tag_settings.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.viewmodel.ComebackViewModel
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.tag_details.presentation.TagDetailsScreen
import com.example.tagplayer.tag_settings.domain.TagSettingsInteractor

class TagSettingsViewModel(
    private val runAsync: RunAsync,
    private val interactor: TagSettingsInteractor,
    private val observable: CustomObservable.All<TagSettingsState>,
    private val mapper: TagSettingsResponse.Mapper,
    private val navigation: Navigation.Navigate,
) : ComebackViewModel(), HandleUiStateUpdates.All<TagSettingsState> {

    fun loadTags() = interactor.tags().map(mapper, viewModelScope)

    fun showTagDialog(tagId: Long? = null) = navigation.update(TagDetailsScreen(tagId))

    fun deleteTag(id: Long) = runAsync.handle(viewModelScope, { it.map(mapper, viewModelScope) }) {
        interactor.remove(id)
    }

    override fun startGettingUpdates(observer: CustomObserver<TagSettingsState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(TagSettingsObserver.Empty)

    override fun clear() = observable.clear()

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }
}