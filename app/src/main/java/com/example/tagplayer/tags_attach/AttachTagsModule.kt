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
import com.example.tagplayer.tags_attach.domain.TagDomain
import com.example.tagplayer.tags_attach.domain.TagsResponse
import com.example.tagplayer.tags_attach.domain.errors.AttachHandleDomainError
import com.example.tagplayer.tags_attach.presentation.AttachTagsObservable
import com.example.tagplayer.tags_attach.presentation.AttachTagsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val attachTagsModule = module {
    viewModel {
        val observable = AttachTagsObservable()
        val repository = AttachTagsRepositoryImpl(
            AttachTagsCacheDatasource.Base(get<TagsDao>(), get<SongsDao>()),
            HandleTry.Base(AttachHandleDomainError.Base)
        )
        val interactor = AttachTagsInteractor.Base(
            repository,
            TagDomain.Mapper.Ui,
            HandleResponse.WithEmpty(
                TagsResponse.Empty,
                get<HandleError<DomainError, String>>()
            ) { error, mapper -> TagsResponse.Error(mapper.handle(error)) }
        )

        AttachTagsViewModel(
            get<RunAsync>(),
            interactor,
            get<Navigation.Navigate>(),
            observable,
            TagsResponse.Mapper.Base(observable, DispatcherList.Base),
            get<HandleDeath>()
        )
    }
}