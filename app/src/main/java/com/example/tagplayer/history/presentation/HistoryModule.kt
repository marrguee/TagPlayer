package com.example.tagplayer.history.presentation

import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.history.data.HistoryCacheDatasource
import com.example.tagplayer.history.data.HistoryRepositoryImpl
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.history.domain.HistoryResponse.HistoryResponseMapper
import com.example.tagplayer.history.domain.SongHistoryInteractor

interface HistoryModule : Module<HistoryViewModel> {
    class Base(private val core: Core) : HistoryModule {
        override fun create(): HistoryViewModel {
            val historyCacheDatasource: HistoryCacheDatasource.Base =
                HistoryCacheDatasource.Base(core.mediaDatabase())
            val historyRepositoryImpl = HistoryRepositoryImpl(
                HandleError.Domain,
                core.foregroundWrapper(),
                historyCacheDatasource,
                SongLastPlayedCrossRef.Mapper.ToDomain
            )
            val historyInteractor: SongHistoryInteractor = SongHistoryInteractor.Base(
                historyRepositoryImpl,
                HandleError.Presentation,
            )
            val historyCommunication = Communication.HistoryCommunication()
            val responseHistoryMapper = HistoryResponseMapper.Base(
                historyCommunication,
                DispatcherList.Base
            )
            return HistoryViewModel(
                historyInteractor,
                historyCommunication,
                responseHistoryMapper
            )
        }
    }
}