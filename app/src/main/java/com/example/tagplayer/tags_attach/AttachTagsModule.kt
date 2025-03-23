package com.example.tagplayer.tags_attach

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.tags_attach.domain.AttachTagsInteractor
import com.example.tagplayer.tags_attach.data.AttachTagsCacheDatasource
import com.example.tagplayer.tags_attach.data.AttachTagsRepositoryImpl
import com.example.tagplayer.tags_attach.domain.TagDomain
import com.example.tagplayer.tags_attach.domain.TagsResponse
import com.example.tagplayer.tags_attach.domain.errors.AttachHandleDomainError
import com.example.tagplayer.tags_attach.presentation.AttachTagsObservable
import com.example.tagplayer.tags_attach.presentation.AttachTagsState
import com.example.tagplayer.tags_attach.presentation.AttachTagsViewModel
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation

class AttachTagsModule(
    core: Core,
    private val clear: ClearViewModel
) : Module<AttachTagsViewModel> {

    private val observable: CustomObservable.All<AttachTagsState> = AttachTagsObservable()
    private val repository = AttachTagsRepositoryImpl(
        AttachTagsCacheDatasource.Base(core.tagDao(), core.songsDao()),
        HandleTry.Base(AttachHandleDomainError.Base)
    )
    private val interactor = AttachTagsInteractor.Base(
        repository,
        TagDomain.Mapper.Ui,
        HandleResponse.WithEmpty(
            TagsResponse.Empty,
            core.handlePresentationError()
        ) { e, handleError ->
            TagsResponse.Error(handleError.handle(e))
        }
    )

    override fun create(): AttachTagsViewModel = AttachTagsViewModel(
        clear,
        RunAsync.Base(DispatcherList.Base),
        interactor,
        Navigation.Base,
        observable,
        TagsResponse.Mapper.Base(observable, DispatcherList.Base),
        HandleDeath.Base(),
    )
}