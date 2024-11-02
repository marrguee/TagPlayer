package com.example.tagplayer.tagsettings.presentation

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.tagsettings.add_tag.AddTagDialogFragment
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.ComebackViewModel
import com.example.tagplayer.main.presentation.ComebackViewModelsModule
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import com.example.tagplayer.tagsettings.domain.TagSettingsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagSettingsViewModel(
    private val interactor: TagSettingsInteractor,
    private val observable: CustomObservable.All<TagSettingsState>,
    private val selectedTag: MutableLiveData<TagSettingsUi?>,
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
        selectedTag.value = tagSettingsUi
    }

    fun deleteTag(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
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