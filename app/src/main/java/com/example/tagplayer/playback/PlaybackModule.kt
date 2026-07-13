package com.example.tagplayer.playback

import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.media_service.HandleMediaExtras
import com.example.tagplayer.core.presentation.ShareRequest
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.home.data.HandleMediaStore
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.playback.data.HandleMediaResult
import com.example.tagplayer.playback.data.PlaybackCacheDatasource
import com.example.tagplayer.playback.data.PlaybackRepositoryImpl
import com.example.tagplayer.playback.domain.PlaybackInteractor
import com.example.tagplayer.playback.domain.SongDetailsResponse
import com.example.tagplayer.playback.domain.TagPlaybackDomain
import com.example.tagplayer.playback.domain.errors.PlaybackHandleDomainError
import com.example.tagplayer.playback.presentation.HandleConnection
import com.example.tagplayer.playback.presentation.PlaybackObservable
import com.example.tagplayer.playback.presentation.PlaybackViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playbackModule = module {
    viewModel {
        val observable = PlaybackObservable()
        val repository = PlaybackRepositoryImpl(
            HandleTry.Base(PlaybackHandleDomainError.Base),
            PlaybackCacheDatasource.Base(
                get<HandleMediaStore>(),
                get<TagsDao>(),
                get<SongsDao>()
            ),
            SongTag.Mapper.ToPlaybackDomain
        )
        val interactor = PlaybackInteractor.Base(
            repository,
            HandleResponse.WithoutEmpty(
                get<HandleError<DomainError, String>>()
            ) { error, mapper -> SongDetailsResponse.Error(mapper.handle(error)) },
            HandleMediaResult.Mapper.Base,
            TagPlaybackDomain.Mapper.ToUi
        )

        PlaybackViewModel(
            get<RunAsync>(),
            observable,
            interactor,
            SongDetailsResponse.Mapper.Base(
                get<ShareRequest>(),
                observable,
                DispatcherList.Base
            ),
            get<Navigation.Navigate>(),
            HandleMediaExtras.Read(),
            HandleConnection.Base(androidContext())
        )
    }
}