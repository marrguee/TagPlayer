package com.example.tagplayer.tag_details

import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.tag_details.data.TagDetailsDatasource
import com.example.tagplayer.tag_details.data.TagDetailsRepositoryImpl
import com.example.tagplayer.tag_details.domain.TagDetailsInteractor
import com.example.tagplayer.tag_details.domain.TagDetailsResponse
import com.example.tagplayer.tag_details.domain.errors.TagDetailsHandleDomainError
import com.example.tagplayer.tag_details.presentation.AddTagViewModel
import com.example.tagplayer.tag_details.presentation.EditTagViewModel
import com.example.tagplayer.tag_details.presentation.TagDetailsObservable
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val tagDetailsModule = module {
    viewModel {
        val observable = TagDetailsObservable()
        val interactor = createInteractor()
        AddTagViewModel(
            get<HandleDeath>(),
            get<RunAsync>(),
            interactor,
            observable
        )
    }

    viewModel {
        val observable = TagDetailsObservable()
        val interactor = createInteractor()
        EditTagViewModel(
            get<HandleDeath>(),
            observable,
            get<RunAsync>(),
            interactor,
            TagDetailsResponse.Mapper.Base(observable),
            get<ManageResources.SongIdError>()
        )
    }
}

private fun org.koin.core.scope.Scope.createInteractor(): TagDetailsInteractor {
    val repository = TagDetailsRepositoryImpl(
        TagDetailsDatasource.Base(get<TagsDao>()),
        HandleTry.Base(TagDetailsHandleDomainError.Base)
    )
    return TagDetailsInteractor.Base(
        repository,
        HandleResponse.WithEmpty(
            TagDetailsResponse.Empty,
            get<HandleError<DomainError, String>>()
        ) { error, mapper -> TagDetailsResponse.Error(mapper.handle(error)) }
    )
}