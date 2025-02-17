package com.example.tagplayer.tag_settings.presentation

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.ComebackViewModelsModule
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import com.example.tagplayer.tag_settings.add_tag.presentation.AddTagDialogFragment
import com.example.tagplayer.tag_settings.domain.TagSettingsInteractor
import kotlinx.coroutines.launch

class TagSettingsViewModel(
    private val dispatcherList: DispatcherList,
    private val interactor: TagSettingsInteractor,
    private val observable: CustomObservable.All<TagSettingsState>,
    private val selectedTag: Selected<TagSettingsUi>,
    private val mapper: TagSettingsResponse.TagSettingsResponseMapper,
    private val navigation: Navigation.Navigate,
    clear: () -> Unit
) : ComebackViewModelsModule(clear), HandleUiStateUpdates.All<TagSettingsState> {

    fun loadTags() {
        interactor.tags().map(mapper, viewModelScope)
    }

    fun showTagDialog(fragmentManager: FragmentManager) {
        AddTagDialogFragment().show(fragmentManager, AddTagDialogFragment::class.simpleName)
    }

    fun editTag(tagSettingsUi: TagSettingsUi) {
        selectedTag.set(tagSettingsUi)
    }

    fun deleteTag(id: Long) {
        viewModelScope.launch(dispatcherList.io()) {
            interactor.removeTag(id)
        }
    }

    override fun startGettingUpdates(observer: CustomObserver<TagSettingsState>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(TagSettingsObserver.Empty)
    }

    override fun clear() {
        observable.clear()
    }

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }
}