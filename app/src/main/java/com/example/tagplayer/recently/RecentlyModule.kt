package com.example.tagplayer.recently

import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.recently.data.RecentlyCacheDatasource
import com.example.tagplayer.recently.data.RecentlyRepositoryImpl
import com.example.tagplayer.recently.domain.RecentlyDomain
import com.example.tagplayer.recently.domain.RecentlyInteractor
import com.example.tagplayer.recently.domain.RecentlyRepository
import com.example.tagplayer.recently.domain.RecentlyResponse
import com.example.tagplayer.recently.domain.errors.RecentlyHandleDomainError
import com.example.tagplayer.recently.presentation.RecentlyObservable
import com.example.tagplayer.recently.presentation.RecentlyViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val recentlyModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<RecentlyObservable> { RecentlyObservable() }

        scoped<RecentlyCacheDatasource> {
            RecentlyCacheDatasource.Base(
                database = get<MediaDatabase>()
            )
        }

        scoped<RecentlyRepository<RecentlyDomain>> {
            RecentlyRepositoryImpl(
                foregroundWrapper = get<ForegroundWrapper>(),
                handleTry = HandleTry.Base(
                    handleError = RecentlyHandleDomainError.Base
                ),
                cacheDatasource = get<RecentlyCacheDatasource>(),
                mapper = SongLastPlayedCrossRef.Mapper.ToDomainRecently
            )
        }

        scoped<RecentlyInteractor> {
            RecentlyInteractor.Base(
                repository = get<RecentlyRepository<RecentlyDomain>>(),
                handleResponse = HandleResponse.WithoutEmpty(
                    handleError = get<HandleError<DomainError, String>>(),
                    errorResponse = { error, mapper ->
                        RecentlyResponse.RecentlyResponseError(
                            msg = mapper.handle(error)
                        )
                    }
                )
            )
        }

        scoped<RecentlyResponse.HistoryResponseMapper> {
            RecentlyResponse.HistoryResponseMapper.Base(
                observable = get<RecentlyObservable>()
            )
        }

        viewModel {
            RecentlyViewModel(
                runAsync = get<RunAsync>(),
                interactor = get<RecentlyInteractor>(),
                observable = get<RecentlyObservable>(),
                mapper = get<RecentlyResponse.HistoryResponseMapper>(),
                navigation = get<Navigation.Navigate>(),
                handleDeath = get<HandleDeath>()
            )
        }
    }
}