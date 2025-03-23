package com.example.tagplayer.recently

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.recently.data.RecentlyCacheDatasource
import com.example.tagplayer.recently.data.RecentlyRepositoryImpl
import com.example.tagplayer.recently.domain.RecentlyInteractor
import com.example.tagplayer.recently.domain.RecentlyResponse
import com.example.tagplayer.recently.domain.RecentlyResponse.HistoryResponseMapper
import com.example.tagplayer.recently.domain.errors.RecentlyHandleDomainError
import com.example.tagplayer.recently.presentation.RecentlyObservable
import com.example.tagplayer.recently.presentation.RecentlyState
import com.example.tagplayer.recently.presentation.RecentlyViewModel

interface RecentlyModule : Module<RecentlyViewModel> {

    class Base(core: Core, private val clear: ClearViewModel) : RecentlyModule {
        private val observable: CustomObservable.All<RecentlyState> = RecentlyObservable()
        private val responseHistoryMapper = HistoryResponseMapper.Base(observable)
        private val recentlyCacheDatasource: RecentlyCacheDatasource.Base =
            RecentlyCacheDatasource.Base(core.mediaDatabase())
        private val recentlyRepositoryImpl = RecentlyRepositoryImpl(
            core.foregroundWrapper(),
            HandleTry.Base(RecentlyHandleDomainError.Base),
            recentlyCacheDatasource,
            SongLastPlayedCrossRef.Mapper.ToDomainRecently
        )
        private val historyInteractor: RecentlyInteractor = RecentlyInteractor.Base(
            recentlyRepositoryImpl,
            HandleResponse.WithoutEmpty(core.handlePresentationError()) { e, handleError ->
                RecentlyResponse.RecentlyResponseError(handleError.handle(e))
            },
        )

        override fun create(): RecentlyViewModel = RecentlyViewModel(
            RunAsync.Base(DispatcherList.Base),
            historyInteractor,
            observable,
            responseHistoryMapper,
            Navigation.Base,
            HandleDeath.Base(),
            clear
        )
    }
}