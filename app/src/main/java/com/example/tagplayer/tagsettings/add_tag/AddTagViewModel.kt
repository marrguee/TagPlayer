package com.example.tagplayer.tagsettings.add_tag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTagViewModel(
    private val interactor: AddTagInteractor,
    private val selectedTag: MutableLiveData<TagSettingsUi?>,
    private val observable: CustomObservable.All<TagDialogState>
) : ViewModel(), HandleUiStateUpdates.All<TagDialogState> {

    fun init() {
        observable.update(
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

    fun clearSelected() {
        selectedTag.value = null
    }

    override fun startGettingUpdates(observer: CustomObserver<TagDialogState>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(AddTagObserver.Empty)
    }

    override fun clear() {
        observable.clear()
    }
}