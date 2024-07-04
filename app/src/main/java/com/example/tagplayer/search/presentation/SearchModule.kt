package com.example.tagplayer.search.presentation

import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.search.data.SearchCacheDatasource
import com.example.tagplayer.search.data.SearchRepositoryImpl
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse.SearchResponseMapper

interface SearchModule : Module<SearchViewModel> {
    class Base(
        private val core: Core,
        private val clear: () -> Unit
    ) : SearchModule {
        override fun create(): SearchViewModel {
            val searchCacheDatasource: SearchCacheDatasource.Base =
                SearchCacheDatasource.Base(core.mediaDatabase())
            val searchRepositoryImpl = SearchRepositoryImpl(
                HandleError.Domain,
                core.foregroundWrapper(),
                searchCacheDatasource,
                Song.Mapper.ToDomain,
                SearchHistoryTable.Mapper.ToDomain
            )
            val searchInteractor: SearchInteractor = SearchInteractor.Base(
                searchRepositoryImpl,
                HandleError.Presentation,
                SearchDomain.Mapper.ToPresentation,
                SongDomain.Mapper.ToPresentation
            )
            val searchCommunication = Communication.SearchCommunication()
            val responseSearchMapper = SearchResponseMapper.Base(
                searchCommunication,
                DispatcherList.Base
            )
            return SearchViewModel(
                searchInteractor,
                searchCommunication,
                responseSearchMapper,
                DispatcherList.Base,
                clear
            )
        }
    }
}