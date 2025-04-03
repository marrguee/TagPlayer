package com.example.tagplayer.playback

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.media_service.HandleMediaExtras
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.playback.data.HandleMediaResult
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.playback.data.PlaybackCacheDatasource
import com.example.tagplayer.playback.data.PlaybackRepositoryImpl
import com.example.tagplayer.playback.domain.errors.PlaybackHandleDomainError
import com.example.tagplayer.playback.domain.PlaybackInteractor
import com.example.tagplayer.playback.domain.PlaybackRepository
import com.example.tagplayer.playback.domain.TagPlaybackDomain
import com.example.tagplayer.playback.domain.SongDetailsResponse
import com.example.tagplayer.playback.presentation.PlaybackObservable
import com.example.tagplayer.playback.presentation.PlaybackViewModel

interface PlaybackModule : Module<PlaybackViewModel> {
    class Base(core: Core) : PlaybackModule {
        private val handleConnection = core.handleConnection()
        private val observable: PlaybackObservable = PlaybackObservable()
        private val cacheDatasource: PlaybackCacheDatasource = PlaybackCacheDatasource.Base(
            core.mediaStoreHandler(),
            core.tagDao(),
            core.songsDao()
        )
        private val repository: PlaybackRepository<TagPlaybackDomain, HandleMediaResult> =
            PlaybackRepositoryImpl(
                HandleTry.Base(PlaybackHandleDomainError.Base),
                cacheDatasource,
                SongTag.Mapper.ToPlaybackDomain
            )
        private val interactor: PlaybackInteractor = PlaybackInteractor.Base(
            repository,
            HandleResponse.WithoutEmpty(core.handlePresentationError()) { e, handleError ->
                SongDetailsResponse.Error(handleError.handle(e))
            },
            HandleMediaResult.Mapper.Base,
            TagPlaybackDomain.Mapper.ToUi
        )
        private val mapper: SongDetailsResponse.Mapper = SongDetailsResponse.Mapper.Base(
            core.shareRequest(),
            observable,
            DispatcherList.Base
        )

        override fun create(): PlaybackViewModel = PlaybackViewModel(
            RunAsync.Base(DispatcherList.Base),
            observable,
            interactor,
            mapper,
            Navigation.Base,
            HandleMediaExtras.Read(),
            handleConnection
        )
    }
}