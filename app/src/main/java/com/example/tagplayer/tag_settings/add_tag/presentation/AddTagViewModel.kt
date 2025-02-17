package com.example.tagplayer.tag_settings.add_tag.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.HandleSaveRestoreState
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.HandleSaveAndRestoreState
import com.example.tagplayer.tag_settings.add_tag.domain.AddTagInteractor
import com.example.tagplayer.tag_settings.presentation.Selected
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTagViewModel(
    private val dispatcherList: DispatcherList,
    private val interactor: AddTagInteractor,
    private val selectedTag: Selected<TagSettingsUi>,
    private val observable: CustomObservable.AllHandleState<TagDialogState>,
    private val handleDeath: HandleDeath,
) : ViewModel(),
    HandleUiStateUpdates.All<TagDialogState>,
    HandleSaveAndRestoreState<TagDialogState> {

    override fun init(bundle: HandleSaveRestoreState.Restore<TagDialogState>) {
        if (bundle.empty()) {
            observable.update(
                if (selectedTag.selected()) TagDialogState.EditMode(selectedTag.get())
                else TagDialogState.AddMode
            )
            handleDeath.handleFirstStart()
        } else if (handleDeath.deathHappened()) {
            observable.restore(bundle)
            handleDeath.handleDeath()
        }
    }

    fun acceptTag(title: String, color: String, dismissCallback: () -> Unit) {
        viewModelScope.launch(dispatcherList.io()) {
            if (!selectedTag.selected()) interactor.addTag(title, color)
            else selectedTag.get().let {
                it.provideId(title, color) { title, color, id ->
                    interactor.addTag(title, color, id)
                }
            }
            withContext(dispatcherList.ui()) {
                dismissCallback.invoke()
            }
        }
    }

    fun clearSelected() {
        selectedTag.clearSelected()
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

    override fun save(bundle: HandleSaveRestoreState.Save<TagDialogState>) {
        observable.save(bundle)
    }
}