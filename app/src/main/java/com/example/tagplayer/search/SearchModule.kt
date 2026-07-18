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
import com.example.tagplayer.search.domain.SearchRepository
import com.example.tagplayer.search.domain.SearchResponse
import com.example.tagplayer.search.domain.errors.SearchHandleDomainError
import com.example.tagplayer.search.presentation.SearchObservable
import com.example.tagplayer.search.presentation.SearchViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val searchModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<SearchObservable> { SearchObservable() }

        scoped<SearchCacheDatasource> {
            SearchCacheDatasource.Base(
                songsDao = get<SongsDao>()
            )
        }

        scoped<SearchRepository<SearchDomain>> {
            SearchRepositoryImpl(
                foregroundWrapper = get<ForegroundWrapper>(),
                handleTry = HandleTry.Base(
                    handleError = SearchHandleDomainError.Base
                ),
                cacheDatasource = get<SearchCacheDatasource>(),
                mapper = Song.Mapper.ToDomainSearch
            )
        }

        scoped<SearchInteractor> {
            SearchInteractor.Base(
                repository = get<SearchRepository<SearchDomain>>(),
                handleResponse = HandleResponse.WithoutEmpty(
                    handleError = get<HandleError<DomainError, String>>(),
                    errorResponse = { error, mapper ->
                        SearchResponse.Error(cause = mapper.handle(error))
                    }
                ),
                mapper = SearchDomain.Mapper.ToUi
            )
        }

        scoped<SearchResponse.Mapper> {
            SearchResponse.Mapper.Base(
                observable = get<SearchObservable>()
            )
        }

        viewModel {
            SearchViewModel(
                runAsync = get<RunAsync>(),
                interactor = get<SearchInteractor>(),
                observable = get<SearchObservable>(),
                mapper = get<SearchResponse.Mapper>(),
                navigation = get<Navigation.Navigate>()
            )
        }
    }
}