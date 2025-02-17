package com.example.tagplayer.search

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.search.data.SearchCacheDatasource
import com.example.tagplayer.search.data.SearchRepositoryImpl
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse.SearchResponseMapper
import com.example.tagplayer.search.domain.SongSearchDomain
import com.example.tagplayer.search.presentation.SearchObservable
import com.example.tagplayer.search.presentation.SearchViewModel

class SearchModule(
    private val core: Core,
    private val clear: ClearViewModel
) : Module<SearchViewModel> {
    override fun create(): SearchViewModel {
        val searchCacheDatasource: SearchCacheDatasource.Base =
            SearchCacheDatasource.Base(core.mediaDatabase())
        val searchRepositoryImpl = SearchRepositoryImpl(
            HandleError.Domain,
            core.foregroundWrapper(),
            searchCacheDatasource,
            Song.Mapper.ToDomainSearch
        )
        val searchInteractor: SearchInteractor = SearchInteractor.Base(
            searchRepositoryImpl,
            HandleError.Presentation,
            SongSearchDomain.Mapper.ToUi
        )
        val observable = SearchObservable()
        val responseSearchMapper = SearchResponseMapper.Base(
            observable,
            DispatcherList.Base
        )
        return SearchViewModel(
            clear,
            DispatcherList.Base,
            searchInteractor,
            observable,
            responseSearchMapper,
            Navigation.Base,
        )
    }
}