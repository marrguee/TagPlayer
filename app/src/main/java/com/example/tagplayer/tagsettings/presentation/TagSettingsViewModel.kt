package com.example.tagplayer.tagsettings.presentation

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.tagsettings.add_tag.AddTagDialogFragment
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.tagsettings.domain.TagSettingsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagSettingsViewModel(
    private val interactor: TagSettingsInteractor,
    private val communication: Communication<TagSettingsState>,
    private val selectedTag: MutableLiveData<TagSettingsUi?>,
    private val mapper: TagSettingsResponse.TagSettingsResponseMapper,
    private val clear: () -> Unit
) : ViewModel() {

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

    fun observe(owner: LifecycleOwner, observer: Observer<in TagSettingsState>) =
        communication.observe(owner, observer)

    override fun onCleared() {
        super.onCleared()
        clear.invoke()
    }
}