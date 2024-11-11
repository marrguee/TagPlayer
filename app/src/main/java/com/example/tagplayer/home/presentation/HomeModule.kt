package com.example.tagplayer.home.presentation

import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.Module
import com.example.tagplayer.home.data.HomeCacheDatasource
import com.example.tagplayer.home.data.HomeRepositoryImpl
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.main.presentation.Navigation

interface HomeModule : Module<HomeViewModel> {
    class Base(
        private val core: Core,
        private val songsFilterPrefs: SharedPrefs.Read<List<Long>>,
        private val tagFiltersObservable: CustomObservable.Mutable<TagFiltersResponse>,
    ) : HomeModule {

        override fun create(): HomeViewModel {
            val observable: CustomObservable.All<HomeState> = HomeObservable()

            val homeCacheDatasource: HomeCacheDatasource.Base =
                HomeCacheDatasource.Base(
                    core.mediaDatabase(),
                    core.foregroundWrapper(),
                    songsFilterPrefs
                )
            val allRepositoryImpl = HomeRepositoryImpl(
                core.foregroundWrapper(),
                HandleError.Domain,
                homeCacheDatasource,
                Song.Mapper.ToDomain
            )
            val homeInteractor: HomeInteractor = HomeInteractor.Base(
                allRepositoryImpl,
                HandleError.Presentation,
                SongDomain.Mapper.ToPresentation,
            )
            val tagFilterMapper = TagFilterMapper.Mapper(
                homeInteractor,
                observable,
                SongsResponse.SongsResponseMapper.Base(observable)
            )
            return HomeViewModel(
                homeInteractor,
                observable,
                tagFiltersObservable,
                tagFilterMapper,
                Navigation.Base,
                HandelDeath.Base()
            )
        }
    }
}