package com.example.tagplayer.home

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.home.data.HandleSort
import com.example.tagplayer.home.data.HomeCacheDatasource
import com.example.tagplayer.home.data.HomeRepositoryImpl
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.home.presentation.HomeObservable
import com.example.tagplayer.home.presentation.HomeState
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.home.presentation.SongsResponse
import com.example.tagplayer.home.presentation.TagFilterMapper
import com.example.tagplayer.home.presentation.TagFiltersState
import com.example.tagplayer.main.presentation.Navigation
import kotlinx.coroutines.flow.MutableStateFlow

class HomeModule(
    private val core: Core,
    private val songsFilterPrefs: SharedPrefs.Read<List<Long>>,
    private val tagFiltersObservable: MutableStateFlow<TagFiltersState>
) : Module<HomeViewModel> {

    override fun create(): HomeViewModel {
        val observable: CustomObservable.All<HomeState> = HomeObservable()

        val homeCacheDatasource: HomeCacheDatasource.Base =
            HomeCacheDatasource.Base(
                core.mediaDatabase(),
                core.foregroundWrapper(),
                songsFilterPrefs,
                HandleSort.Base(core.songsDao())
            )
        val allRepositoryImpl = HomeRepositoryImpl(
            core.foregroundWrapper(),
            HandleError.Domain,
            homeCacheDatasource,
            Song.Mapper.ToDomain,
            SongLastPlayedCrossRef.Mapper.ToDomainHome
        )
        val homeInteractor: HomeInteractor = HomeInteractor.Base(
            allRepositoryImpl,
            HandleError.Presentation,
            SongDomain.Mapper.ToPresentation(),
        )
        val tagFilterMapper = TagFilterMapper.Mapper(
            homeInteractor,
            observable,
            SongsResponse.SongsResponseMapper.Base(observable)
        )
        val songResponseMapper = SongsResponse.SongsResponseMapper.Base(observable)

        return HomeViewModel(
            DispatcherList.Base,
            homeInteractor,
            observable,
            tagFiltersObservable,
            tagFilterMapper,
            songResponseMapper,
            Navigation.Base,
            HandleDeath.Base()
        )
    }
}