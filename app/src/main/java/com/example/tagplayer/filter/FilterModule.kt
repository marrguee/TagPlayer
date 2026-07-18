package com.example.tagplayer.filter

import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.filter.data.FilterCacheDatasource
import com.example.tagplayer.filter.data.FilterRepositoryImpl
import com.example.tagplayer.filter.domain.FilterDomain
import com.example.tagplayer.filter.domain.FilterInteractor
import com.example.tagplayer.filter.domain.FilterRepository
import com.example.tagplayer.filter.domain.FilterResponse
import com.example.tagplayer.filter.domain.errors.FilterHandleDomainError
import com.example.tagplayer.filter.presentation.FilterObservable
import com.example.tagplayer.filter.presentation.FilterViewModel
import com.example.tagplayer.main.presentation.navigation.Navigation
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val filterModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<FilterObservable> { FilterObservable() }

        scoped<FilterRepository<FilterDomain>> {
            FilterRepositoryImpl(
                handleTry = HandleTry.Base(
                    handleError = FilterHandleDomainError.Base
                ),
                cacheDatasource = FilterCacheDatasource.Base(
                    tagsDao = get<TagsDao>()
                ),
                mapper = SongTag.Mapper.ToFilterDomain
            )
        }

        scoped<FilterInteractor> {
            FilterInteractor.Base(
                repository = get<FilterRepository<FilterDomain>>(),
                mapper = FilterDomain.Mapper.Ui,
                handleResponse = HandleResponse.WithEmpty(
                    empty = FilterResponse.Empty,
                    handleError = get<HandleError<DomainError, String>>(),
                    errorResponse = { error, mapper ->
                        FilterResponse.Error(error = mapper.handle(error))
                    }
                )
            )
        }

        scoped<FilterResponse.Mapper> {
            FilterResponse.Mapper.Base(
                observable = get<FilterObservable>(),
                dispatcherList = DispatcherList.Base
            )
        }

        viewModel {
            FilterViewModel(
                runAsync = get<RunAsync>(),
                observable = get<FilterObservable>(),
                interactor = get<FilterInteractor>(),
                mapper = get<FilterResponse.Mapper>(),
                navigation = get<Navigation.Navigate>(),
                handleDeath = get<HandleDeath>()
            )
        }
    }
}