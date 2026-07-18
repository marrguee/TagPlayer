package com.example.tagplayer.tags_attach

import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.tags_attach.data.AttachTagsCacheDatasource
import com.example.tagplayer.tags_attach.data.AttachTagsRepositoryImpl
import com.example.tagplayer.tags_attach.domain.AttachTagsInteractor
import com.example.tagplayer.tags_attach.domain.AttachTagsRepository
import com.example.tagplayer.tags_attach.domain.TagDomain
import com.example.tagplayer.tags_attach.domain.TagsResponse
import com.example.tagplayer.tags_attach.domain.errors.AttachHandleDomainError
import com.example.tagplayer.tags_attach.presentation.AttachTagsObservable
import com.example.tagplayer.tags_attach.presentation.AttachTagsViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val attachTagsModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<AttachTagsObservable> { AttachTagsObservable() }

        scoped<AttachTagsCacheDatasource> {
            AttachTagsCacheDatasource.Base(
                tagsDao = get<TagsDao>(),
                songsDao = get<SongsDao>()
            )
        }

        scoped<AttachTagsRepository<TagDomain>> {
            AttachTagsRepositoryImpl(
                cacheDatasource = get<AttachTagsCacheDatasource>(),
                handleTry = HandleTry.Base(
                    handleError = AttachHandleDomainError.Base
                )
            )
        }

        scoped<AttachTagsInteractor> {
            AttachTagsInteractor.Base(
                repository = get<AttachTagsRepository<TagDomain>>(),
                mapper = TagDomain.Mapper.Ui,
                handleResponse = HandleResponse.WithEmpty(
                    empty = TagsResponse.Empty,
                    handleError = get<HandleError<DomainError, String>>(),
                    errorResponse = { error, mapper ->
                        TagsResponse.Error(error = mapper.handle(error))
                    }
                )
            )
        }

        scoped<TagsResponse.Mapper> {
            TagsResponse.Mapper.Base(
                observable = get<AttachTagsObservable>(),
                dispatcherList = DispatcherList.Base
            )
        }

        viewModel {
            AttachTagsViewModel(
                runAsync = get<RunAsync>(),
                interactor = get<AttachTagsInteractor>(),
                navigation = get<Navigation.Navigate>(),
                observable = get<AttachTagsObservable>(),
                mapper = get<TagsResponse.Mapper>(),
                handleDeath = get<HandleDeath>()
            )
        }
    }
}