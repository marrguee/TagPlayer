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
import com.example.tagplayer.playback.domain.PlaybackRepository
import com.example.tagplayer.playback.domain.SongDetailsResponse
import com.example.tagplayer.playback.domain.TagPlaybackDomain
import com.example.tagplayer.playback.domain.errors.PlaybackHandleDomainError
import com.example.tagplayer.playback.presentation.HandleConnection
import com.example.tagplayer.playback.presentation.PlaybackObservable
import com.example.tagplayer.playback.presentation.PlaybackViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val playbackModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<PlaybackObservable> { PlaybackObservable() }

        scoped<PlaybackCacheDatasource> {
            PlaybackCacheDatasource.Base(
                handleMediaStore = get<HandleMediaStore>(),
                tagsDao = get<TagsDao>(),
                songsDao = get<SongsDao>()
            )
        }

        scoped<PlaybackRepository<TagPlaybackDomain, HandleMediaResult>> {
            PlaybackRepositoryImpl(
                handleTry = HandleTry.Base(
                    handleError = PlaybackHandleDomainError.Base
                ),
                cacheDatasource = get<PlaybackCacheDatasource>(),
                mapper = SongTag.Mapper.ToPlaybackDomain
            )
        }

        scoped<PlaybackInteractor> {
            PlaybackInteractor.Base(
                repository = get<PlaybackRepository<TagPlaybackDomain, HandleMediaResult>>(),
                handleResponse = HandleResponse.WithoutEmpty(
                    handleError = get<HandleError<DomainError, String>>(),
                    errorResponse = { error, mapper ->
                        SongDetailsResponse.Error(error = mapper.handle(error))
                    }
                ),
                mapper = HandleMediaResult.Mapper.Base,
                tagMapper = TagPlaybackDomain.Mapper.ToUi
            )
        }

        scoped<SongDetailsResponse.Mapper> {
            SongDetailsResponse.Mapper.Base(
                shareRequest = get<ShareRequest>(),
                observable = get<PlaybackObservable>(),
                dispatcherList = DispatcherList.Base
            )
        }

        scoped<HandleMediaExtras.Obtain> { HandleMediaExtras.Read() }

        scoped<HandleConnection> {
            HandleConnection.Base(context = androidContext())
        }

        viewModel {
            PlaybackViewModel(
                runAsync = get<RunAsync>(),
                observable = get<PlaybackObservable>(),
                interactor = get<PlaybackInteractor>(),
                mapper = get<SongDetailsResponse.Mapper>(),
                navigation = get<Navigation.Navigate>(),
                mediaExtras = get<HandleMediaExtras.Obtain>(),
                handleConnection = get<HandleConnection>()
            )
        }
    }
}