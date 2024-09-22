package com.example.tagplayer.recently.presentation

import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.recently.data.RecentlyCacheDatasource
import com.example.tagplayer.recently.data.RecentlyRepositoryImpl
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.recently.domain.RecentlyResponse.HistoryResponseMapper
import com.example.tagplayer.recently.domain.RecentlyInteractor

interface RecentlyModule : Module<RecentlyViewModel> {
    class Base(
        private val core: Core,
        private val clear: ClearViewModel
    ) : RecentlyModule {
        override fun create(): RecentlyViewModel {
            val recentlyCacheDatasource: RecentlyCacheDatasource.Base =
                RecentlyCacheDatasource.Base(core.mediaDatabase())
            val recentlyRepositoryImpl = RecentlyRepositoryImpl(
                HandleError.Domain,
                core.foregroundWrapper(),
                recentlyCacheDatasource,
                SongLastPlayedCrossRef.Mapper.ToDomain
            )
            val historyInteractor: RecentlyInteractor = RecentlyInteractor.Base(
                recentlyRepositoryImpl,
                HandleError.Presentation,
            )
            val observable = RecentlyObservable()
            val responseHistoryMapper = HistoryResponseMapper.Base(
                observable,
                DispatcherList.Base
            )
            return RecentlyViewModel(
                historyInteractor,
                observable,
                responseHistoryMapper,
                Navigation.Base,
                clear
            )
        }
    }
}