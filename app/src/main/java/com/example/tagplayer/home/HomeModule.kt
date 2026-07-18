package com.example.tagplayer.home

import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.dao.LastPlayedDao
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.home.data.HomeCacheDatasource
import com.example.tagplayer.home.data.HomeRepositoryImpl
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.HomeRepository
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.home.domain.SongsResponse
import com.example.tagplayer.home.domain.SortType
import com.example.tagplayer.home.domain.errors.HomeHandleDomainError
import com.example.tagplayer.home.presentation.HandleDeclineText
import com.example.tagplayer.home.presentation.HomeObservable
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.main.presentation.navigation.Navigation
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val homeModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<HomeObservable> { HomeObservable() }

        scoped<HomeCacheDatasource> {
            HomeCacheDatasource.Base(
                lastPlayed = get<LastPlayedDao>(),
                songsDao = get<SongsDao>()
            )
        }

        scoped<HomeRepository<SongDomain>> {
            HomeRepositoryImpl(
                foregroundWrapper = get<ForegroundWrapper>(),
                handleTry = HandleTry.Base(
                    handleError = HomeHandleDomainError.Base
                ),
                cacheDatasource = get<HomeCacheDatasource>(),
                mapper = Song.Mapper.ToDomain,
                recentlyModelMapper = SongLastPlayedCrossRef.Mapper.ToDomainHome,
            )
        }

        scoped<HomeInteractor> {
            HomeInteractor.Base(
                repository = get<HomeRepository<SongDomain>>(),
                handleResponse = HandleResponse.WithoutEmpty(
                    handleError = get<HandleError<DomainError, String>>(),
                    errorResponse = { error, mapper ->
                        SongsResponse.Error(error = mapper.handle(error))
                    }
                ),
                mapper = SongDomain.Mapper.Presentation,
                sortMapper = SortType.Mapper.Base(
                    repository = get<HomeRepository<SongDomain>>(),
                    mapper = SongDomain.Mapper.Presentation
                )
            )
        }

        viewModel {
            HomeViewModel(
                runAsync = get<RunAsync>(),
                interactor = get<HomeInteractor>(),
                observable = get<HomeObservable>(),
                mapper = SongsResponse.Mapper.Base(
                    observable = get<HomeObservable>(),
                    dispatcherList = DispatcherList.Base
                ),
                navigation = get<Navigation.Navigate>(),
                handleDecline = HandleDeclineText.Factory(
                    manageStrings = get<ManageResources.Strings>()
                )
            )
        }
    }
}