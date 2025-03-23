package com.example.tagplayer.tag_details.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.tag_details.domain.TagDetailsInteractor
import com.example.tagplayer.tag_details.domain.TagDetailsResponse
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation

class EditTagViewModel(
    clear: ClearViewModel,
    handleDeath: HandleDeath,
    navigation: Navigation.Navigate,
    private val observable: CustomObservable.All<TagDialogState>,
    private val runAsync: RunAsync,
    private val interactor: TagDetailsInteractor,
    private val mapper: TagDetailsResponse.Mapper,
    private val manageResources: ManageResources
) : TagViewModel(clear, observable, navigation, handleDeath) {

    private var tagId: Long = DEFAULT_ID

    override val initBlock: (Long?) -> Unit = { id ->
        if (id == null) {
            observable.update(TagDialogState.Error(manageResources.retrieveIdError()))
            comeback()
        } else {
            tagId = id
            runAsync.handle(viewModelScope, { it.map(mapper) }) { interactor.tag(tagId) }
        }
    }

    override fun accept(title: String, color: String, dismissCallback: () -> Unit) =
        runAsync.handle(viewModelScope, { dismissCallback.invoke() }) {
            interactor.add(title, color, tagId)
        }

    companion object {
        private const val DEFAULT_ID = Long.MIN_VALUE
    }
}