package com.example.tagplayer.all.presentation

import com.example.tagplayer.all.domain.HomeInteractor
import com.example.tagplayer.all.domain.HomeResponse.HomeResponseMapper
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.HomeCacheDatasource
import com.example.tagplayer.all.data.HomeRepositoryImpl
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.main.presentation.Navigation

interface HomeModule : Module<HomeViewModel> {
    class Base(private val core: Core) : HomeModule {

        override fun create(): HomeViewModel {
            val allCommunication = Communication.AllCommunication()
            val responseAllMapper = HomeResponseMapper.Base(
                allCommunication,
                DispatcherList.Base
            )
            val homeCacheDatasource: HomeCacheDatasource.Base =
                HomeCacheDatasource.Base(core.mediaDatabase())
            val allRepositoryImpl = HomeRepositoryImpl(
                HandleError.Domain,
                core.foregroundWrapper(),
                homeCacheDatasource,
                Song.Mapper.ToDomain
            )
            val homeInteractor: HomeInteractor = HomeInteractor.Base(
                allRepositoryImpl,
                HandleError.Presentation,
                SongDomain.Mapper.ToPresentation,
            )
            return HomeViewModel(
                homeInteractor,
                allCommunication,
                responseAllMapper,
                Navigation.Base
            )
        }
    }
}