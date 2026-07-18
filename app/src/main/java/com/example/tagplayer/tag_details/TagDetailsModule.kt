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
import com.example.tagplayer.tag_details.domain.TagDetailsRepository
import com.example.tagplayer.tag_details.domain.TagDetailsResponse
import com.example.tagplayer.tag_details.domain.errors.TagDetailsHandleDomainError
import com.example.tagplayer.tag_details.presentation.AddTagViewModel
import com.example.tagplayer.tag_details.presentation.EditTagViewModel
import com.example.tagplayer.tag_details.presentation.TagDetailsObservable
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val tagDetailsModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<TagDetailsObservable> { TagDetailsObservable() }

        scoped<TagDetailsDatasource> {
            TagDetailsDatasource.Base(
                tagsDao = get<TagsDao>()
            )
        }

        scoped<TagDetailsRepository> {
            TagDetailsRepositoryImpl(
                cacheDatasource = get<TagDetailsDatasource>(),
                handleTry = HandleTry.Base(
                    handleError = TagDetailsHandleDomainError.Base
                )
            )
        }

        scoped<TagDetailsInteractor> {
            TagDetailsInteractor.Base(
                repository = get<TagDetailsRepository>(),
                handleResponse = HandleResponse.WithEmpty(
                    empty = TagDetailsResponse.Empty,
                    handleError = get<HandleError<DomainError, String>>(),
                    errorResponse = { error, mapper ->
                        TagDetailsResponse.Error(error = mapper.handle(error))
                    }
                )
            )
        }

        scoped<TagDetailsResponse.Mapper> {
            TagDetailsResponse.Mapper.Base(
                observable = get<TagDetailsObservable>()
            )
        }

        viewModel {
            AddTagViewModel(
                handleDeath = get<HandleDeath>(),
                runAsync = get<RunAsync>(),
                interactor = get<TagDetailsInteractor>(),
                observable = get<TagDetailsObservable>()
            )
        }

        viewModel {
            EditTagViewModel(
                handleDeath = get<HandleDeath>(),
                observable = get<TagDetailsObservable>(),
                runAsync = get<RunAsync>(),
                interactor = get<TagDetailsInteractor>(),
                mapper = get<TagDetailsResponse.Mapper>(),
                manageResources = get<ManageResources.SongIdError>()
            )
        }
    }
}