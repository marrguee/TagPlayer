package com.example.tagplayer.tag_details

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.tag_details.data.TagDetailsDatasource
import com.example.tagplayer.tag_details.data.TagDetailsRepositoryImpl
import com.example.tagplayer.tag_details.domain.TagDetailsInteractor
import com.example.tagplayer.tag_details.domain.TagDetailsRepository
import com.example.tagplayer.tag_details.domain.TagDetailsResponse
import com.example.tagplayer.tag_details.domain.errors.TagDetailsHandleDomainError
import com.example.tagplayer.tag_details.presentation.AddTagViewModel
import com.example.tagplayer.tag_details.presentation.EditTagViewModel
import com.example.tagplayer.tag_details.presentation.TagDetailsObservable
import com.example.tagplayer.tag_details.presentation.TagViewModel

class TagDetailsModule(
    private val core: Core,
    private val clear: ClearViewModel,
    private val clazz: Class<out ViewModel>
) : Module<TagViewModel> {
    private val observable = TagDetailsObservable()
    private val repository: TagDetailsRepository = TagDetailsRepositoryImpl(
        TagDetailsDatasource.Base(core.tagDao()),
        HandleTry.Base(TagDetailsHandleDomainError.Base)
    )
    private val interactor: TagDetailsInteractor = TagDetailsInteractor.Base(
        repository,
        HandleResponse.WithEmpty(
            TagDetailsResponse.Empty, core.handlePresentationError()
        ) { e, handleError ->
            TagDetailsResponse.Error(handleError.handle(e))
        }
    )

    override fun create(): TagViewModel = when(clazz) {

        AddTagViewModel::class.java -> AddTagViewModel(
            clear,
            HandleDeath.Base(),
            Navigation.Base,
            RunAsync.Base(DispatcherList.Base),
            interactor,
            observable,
        )

        EditTagViewModel::class.java -> EditTagViewModel(
            clear,
            HandleDeath.Base(),
            Navigation.Base,
            observable,
            RunAsync.Base(DispatcherList.Base),
            interactor,
            TagDetailsResponse.Mapper.Base(observable),
            core.manageRecourses()
        )

        else -> throw IllegalArgumentException()
    }
}