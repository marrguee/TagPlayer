package com.example.tagplayer.home

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.home.data.HomeCacheDatasource
import com.example.tagplayer.home.data.HomeRepositoryImpl
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.home.domain.SongsResponse
import com.example.tagplayer.home.domain.errors.HomeHandleDomainError
import com.example.tagplayer.home.presentation.HomeObservable
import com.example.tagplayer.home.presentation.HomeState
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.home.domain.SortType
import com.example.tagplayer.home.presentation.HandleDeclineText
import com.example.tagplayer.main.presentation.navigation.Navigation

class HomeModule(core: Core) : Module<HomeViewModel> {
    private val observable: CustomObservable.All<HomeState> = HomeObservable()
    private val homeCacheDatasource: HomeCacheDatasource.Base = HomeCacheDatasource.Base(
        core.lastPlayedDao(),
        core.songsDao()
    )
    private val repository = HomeRepositoryImpl(
        core.foregroundWrapper(),
        HandleTry.Base(HomeHandleDomainError.Base),
        homeCacheDatasource,
        Song.Mapper.ToDomain,
        SongLastPlayedCrossRef.Mapper.ToDomainHome
    )
    private val homeInteractor: HomeInteractor = HomeInteractor.Base(
        repository,
        HandleResponse.WithoutEmpty(core.handlePresentationError()) { e, handleError ->
            SongsResponse.Error(handleError.handle(e))
        },
        SongDomain.Mapper.Presentation,
        SortType.Mapper.Base(repository, SongDomain.Mapper.Presentation)
    )
    private val songResponseMapper = SongsResponse.Mapper.Base(
        observable,
        DispatcherList.Base
    )
    private val factory = HandleDeclineText.Factory(core.manageRecourses())
    override fun create(): HomeViewModel = HomeViewModel(
        RunAsync.Base(DispatcherList.Base),
        homeInteractor,
        observable,
        songResponseMapper,
        Navigation.Base,
        factory
    )
}