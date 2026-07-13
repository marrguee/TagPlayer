package com.example.tagplayer.search

import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.search.data.SearchCacheDatasource
import com.example.tagplayer.search.data.SearchRepositoryImpl
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse
import com.example.tagplayer.search.domain.errors.SearchHandleDomainError
import com.example.tagplayer.search.presentation.SearchObservable
import com.example.tagplayer.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel {
        val observable = SearchObservable()
        val repository = SearchRepositoryImpl(
            get<ForegroundWrapper>(),
            HandleTry.Base(SearchHandleDomainError.Base),
            SearchCacheDatasource.Base(get<SongsDao>()),
            Song.Mapper.ToDomainSearch
        )
        val interactor = SearchInteractor.Base(
            repository,
            HandleResponse.WithoutEmpty(
                get<HandleError<DomainError, String>>()
            ) { error, mapper -> SearchResponse.Error(mapper.handle(error)) },
            SearchDomain.Mapper.ToUi
        )

        SearchViewModel(
            get<RunAsync>(),
            interactor,
            observable,
            SearchResponse.Mapper.Base(observable),
            get<Navigation.Navigate>()
        )
    }
}