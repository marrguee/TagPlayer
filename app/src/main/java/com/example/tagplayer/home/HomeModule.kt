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
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.home.domain.SongsResponse
import com.example.tagplayer.home.domain.SortType
import com.example.tagplayer.home.domain.errors.HomeHandleDomainError
import com.example.tagplayer.home.presentation.HandleDeclineText
import com.example.tagplayer.home.presentation.HomeObservable
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.main.presentation.navigation.Navigation
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel {
        val observable = HomeObservable()
        val cacheDatasource = HomeCacheDatasource.Base(
            get<LastPlayedDao>(),
            get<SongsDao>()
        )
        val repository = HomeRepositoryImpl(
            get<ForegroundWrapper>(),
            HandleTry.Base(HomeHandleDomainError.Base),
            cacheDatasource,
            Song.Mapper.ToDomain,
            SongLastPlayedCrossRef.Mapper.ToDomainHome
        )
        val interactor = HomeInteractor.Base(
            repository,
            HandleResponse.WithoutEmpty(
                get<HandleError<DomainError, String>>()
            ) { error, mapper -> SongsResponse.Error(mapper.handle(error)) },
            SongDomain.Mapper.Presentation,
            SortType.Mapper.Base(repository, SongDomain.Mapper.Presentation)
        )

        HomeViewModel(
            get<RunAsync>(),
            interactor,
            observable,
            SongsResponse.Mapper.Base(observable, DispatcherList.Base),
            get<Navigation.Navigate>(),
            HandleDeclineText.Factory(get<ManageResources.Strings>())
        )
    }
}