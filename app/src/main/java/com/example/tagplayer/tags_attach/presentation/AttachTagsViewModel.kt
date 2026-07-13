package com.example.tagplayer.tags_attach.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.viewmodel.ComebackViewModel
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.tags_attach.domain.AttachTagsInteractor
import com.example.tagplayer.tags_attach.domain.TagsResponse

class AttachTagsViewModel(
    private val runAsync: RunAsync,
    private val interactor: AttachTagsInteractor,
    private val navigation: Navigation.Navigate,
    private val observable: CustomObservable.All<AttachTagsState>,
    private val mapper: TagsResponse.Mapper,
    private val handleDeath: HandleDeath,
) : ComebackViewModel(), HandleUiStateUpdates.All<AttachTagsState> {
    private var songId: Long = Long.MIN_VALUE
    private val uiBlock: (TagsResponse) -> Unit = { it.map(mapper, viewModelScope) }

    fun init(id: Long) {
        if (handleDeath.deathHappened()) {
            songId = id
            runAsync.handle(viewModelScope, uiBlock) { interactor.tags(songId) }
            handleDeath.handleDeath()
        }
    }

    fun removeFromOwned(tagId: Long) = runAsync.handle(viewModelScope, uiBlock) {
        interactor.remove(songId, tagId)
    }

    fun addToOwned(tagId: Long) = runAsync.handle(viewModelScope, uiBlock) {
        interactor.add(songId, tagId)
    }

    override fun startGettingUpdates(observer: CustomObserver<AttachTagsState>) =
        observable.updateObserver(observer)

    override fun stopGettingUpdates() = observable.updateObserver(AttachTagsObserver.Empty)

    override fun clear() = observable.clear()

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }
}