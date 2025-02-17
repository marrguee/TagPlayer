package com.example.tagplayer.playback

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.HandleMediaExtras
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.home.data.HandleMediaResult
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.playback.data.PlaybackCacheDatasource
import com.example.tagplayer.playback.data.PlaybackRepositoryImpl
import com.example.tagplayer.playback.domain.HandleSongResult
import com.example.tagplayer.playback.domain.PlaybackControlInteractor
import com.example.tagplayer.playback.domain.PlaybackRepository
import com.example.tagplayer.playback.domain.TagPlaybackDomain
import com.example.tagplayer.playback.domain.TagsResult
import com.example.tagplayer.playback.presentation.PlaybackControlObservable
import com.example.tagplayer.playback.presentation.PlaybackViewModel


interface PlaybackControlModule : Module<PlaybackViewModel> {
    class Base(
        core: Core
    ) : PlaybackControlModule {
        private val observable: PlaybackControlObservable = PlaybackControlObservable()
        private val cacheDatasource: PlaybackCacheDatasource = PlaybackCacheDatasource
            .Base(core.mediaStoreHandler(), core.tagDao())
        private val repository: PlaybackRepository<TagPlaybackDomain> = PlaybackRepositoryImpl(
            cacheDatasource,
            HandleError.Domain,
            SongTag.Mapper.ToPlaybackDomain
        )
        private val interactor: PlaybackControlInteractor = PlaybackControlInteractor.Base(
            repository,
            HandleMediaResult.Mapper.Base,
            HandleError.Presentation,
            TagPlaybackDomain.Mapper.ToUi
        )

        override fun create(): PlaybackViewModel =
            PlaybackViewModel(
                DispatcherList.Base,
                observable,
                interactor,
                HandleSongResult.Mapper.Base(observable),
                TagsResult.Mapper.Base(observable, DispatcherList.Base),
                Navigation.Base,
                HandleMediaExtras.Base
            )
    }
}