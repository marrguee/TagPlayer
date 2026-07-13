package com.example.tagplayer.recently

import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.recently.data.RecentlyCacheDatasource
import com.example.tagplayer.recently.data.RecentlyRepositoryImpl
import com.example.tagplayer.recently.domain.RecentlyInteractor
import com.example.tagplayer.recently.domain.RecentlyResponse
import com.example.tagplayer.recently.domain.errors.RecentlyHandleDomainError
import com.example.tagplayer.recently.presentation.RecentlyObservable
import com.example.tagplayer.recently.presentation.RecentlyViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val recentlyModule = module {
    viewModel {
        val observable = RecentlyObservable()
        val repository = RecentlyRepositoryImpl(
            get<ForegroundWrapper>(),
            HandleTry.Base(RecentlyHandleDomainError.Base),
            RecentlyCacheDatasource.Base(get<MediaDatabase>()),
            SongLastPlayedCrossRef.Mapper.ToDomainRecently
        )
        val interactor = RecentlyInteractor.Base(
            repository,
            HandleResponse.WithoutEmpty(
                get<HandleError<DomainError, String>>()
            ) { error, mapper ->
                RecentlyResponse.RecentlyResponseError(mapper.handle(error))
            }
        )

        RecentlyViewModel(
            get<RunAsync>(),
            interactor,
            observable,
            RecentlyResponse.HistoryResponseMapper.Base(observable),
            get<Navigation.Navigate>(),
            get<HandleDeath>()
        )
    }
}