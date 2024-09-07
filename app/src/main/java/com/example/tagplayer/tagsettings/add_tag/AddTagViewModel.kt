package com.example.tagplayer.tagsettings.add_tag

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.tagsettings.domain.TagDomain
import com.example.tagplayer.tagsettings.presentation.TagSettingsState
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTagViewModel(
    private val interactor: AddTagInteractor,
    private val selectedTag: MutableLiveData<TagSettingsUi?>,
    private val communication: Communication<TagDialogState>
) : ViewModel() {

    fun init() {
        communication.update(
            if (selectedTag.value != null) TagDialogState.EditMode(selectedTag.value!!)
            else TagDialogState.AddMode
        )
    }

    fun addTag(title: String, dismissCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val temporaryColor = "#000000"
            if (selectedTag.value == null) interactor.addTag(title, temporaryColor)
            else selectedTag.value?.let {
                it.provideId(title, temporaryColor) { title, color, id ->
                    interactor.addTag(title, color, id)
                }
            }
            withContext(Dispatchers.Main) {
                dismissCallback.invoke()
            }
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in TagDialogState>) =
        communication.observe(owner, observer)

    fun clearSelected() {
        selectedTag.value = null
    }
}