package com.example.tagplayer.home.presentation

import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.HomeResponse.HomeResponseMapper
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.Module
import com.example.tagplayer.home.data.HomeCacheDatasource
import com.example.tagplayer.home.data.HomeRepositoryImpl
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.filter_by_tags.SharedPrefs
import com.example.tagplayer.main.presentation.Navigation

interface HomeModule : Module<HomeViewModel> {
    class Base(
        private val core: Core,
        private val sharedPrefs: SharedPrefs.Read<List<Long>>,
        private val selectedTagsObservable: CustomObservable.All<List<Long>>,
    ) : HomeModule {

        override fun create(): HomeViewModel {
            val observable: CustomObservable.All<HomeState> = HomeObservable()
            val responseAllMapper = HomeResponseMapper.Base(
                observable,
                DispatcherList.Base
            )
            val homeCacheDatasource: HomeCacheDatasource.Base =
                HomeCacheDatasource.Base(core.mediaDatabase(), sharedPrefs)
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
                observable,
                selectedTagsObservable,
                responseAllMapper,
                Navigation.Base
            )
        }
    }
}