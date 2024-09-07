package com.example.tagplayer.recently.presentation

import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.recently.data.RecentlyCacheDatasource
import com.example.tagplayer.recently.data.RecentlyRepositoryImpl
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.recently.domain.RecentlyResponse.HistoryResponseMapper
import com.example.tagplayer.recently.domain.RecentlyInteractor

interface RecentlyModule : Module<RecentlyViewModel> {
    class Base(private val core: Core) : RecentlyModule {
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
            val historyCommunication = Communication.HistoryCommunication()
            val responseHistoryMapper = HistoryResponseMapper.Base(
                historyCommunication,
                DispatcherList.Base
            )
            return RecentlyViewModel(
                historyInteractor,
                historyCommunication,
                responseHistoryMapper
            )
        }
    }
}