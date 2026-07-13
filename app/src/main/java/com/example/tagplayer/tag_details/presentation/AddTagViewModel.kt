package com.example.tagplayer.tag_details.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.tag_details.domain.TagDetailsInteractor

class AddTagViewModel(
    handleDeath: HandleDeath,
    private val runAsync: RunAsync,
    private val interactor: TagDetailsInteractor,
    private val observable: CustomObservable.All<TagDialogState>,
) : TagViewModel(observable, handleDeath) {

    override val initBlock: (Long?) -> Unit = {
        observable.update(TagDialogState.AddMode)
    }

    override fun accept(title: String, color: String, dismissCallback: () -> Unit) =
        runAsync.handle(viewModelScope, { dismissCallback.invoke() }) {
            interactor.add(title, color)
        }
}