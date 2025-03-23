package com.example.tagplayer.search

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.search.data.SearchCacheDatasource
import com.example.tagplayer.search.data.SearchRepositoryImpl
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse.Mapper
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.domain.SearchResponse
import com.example.tagplayer.search.domain.errors.SearchHandleDomainError
import com.example.tagplayer.search.presentation.SearchObservable
import com.example.tagplayer.search.presentation.SearchViewModel

class SearchModule(core: Core, private val clear: ClearViewModel) : Module<SearchViewModel> {
    private val observable = SearchObservable()
    private val responseSearchMapper = Mapper.Base(observable)
    private val searchCacheDatasource: SearchCacheDatasource =
        SearchCacheDatasource.Base(core.songsDao())
    private val searchRepositoryImpl = SearchRepositoryImpl(
        core.foregroundWrapper(),
        HandleTry.Base(SearchHandleDomainError.Base),
        searchCacheDatasource,
        Song.Mapper.ToDomainSearch
    )
    private val searchInteractor: SearchInteractor = SearchInteractor.Base(
        searchRepositoryImpl,
        HandleResponse.WithoutEmpty(core.handlePresentationError()) { e, handleError ->
            SearchResponse.Error(handleError.handle(e))
        },
        SearchDomain.Mapper.ToUi
    )

    override fun create(): SearchViewModel = SearchViewModel(
        clear,
        RunAsync.Base(DispatcherList.Base),
        searchInteractor,
        observable,
        responseSearchMapper,
        Navigation.Base,
    )
}