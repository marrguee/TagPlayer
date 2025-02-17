package com.example.tagplayer.recently

import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.Module
import com.example.tagplayer.recently.data.RecentlyCacheDatasource
import com.example.tagplayer.recently.data.RecentlyRepositoryImpl
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.recently.domain.RecentlyResponse.HistoryResponseMapper
import com.example.tagplayer.recently.domain.RecentlyInteractor
import com.example.tagplayer.recently.presentation.RecentlyObservable
import com.example.tagplayer.recently.presentation.RecentlyState
import com.example.tagplayer.recently.presentation.RecentlyViewModel

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
                SongLastPlayedCrossRef.Mapper.ToDomainRecently
            )
            val historyInteractor: RecentlyInteractor = RecentlyInteractor.Base(
                recentlyRepositoryImpl,
                HandleError.Presentation,
            )
            val observable: CustomObservable.AllHandleState<RecentlyState> = RecentlyObservable()
            val responseHistoryMapper = HistoryResponseMapper.Base(
                observable,
                DispatcherList.Base
            )
            return RecentlyViewModel(
                DispatcherList.Base,
                historyInteractor,
                observable,
                responseHistoryMapper,
                Navigation.Base,
                HandleDeath.Base(),
                clear
            )
        }
    }
}