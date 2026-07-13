package com.example.tagplayer.core.di

import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.work.WorkManager
import com.example.tagplayer.R
import com.example.tagplayer.core.data.FetchSongWorker
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.MediaWorker
import com.example.tagplayer.core.data.PlaySongWorker
import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.dao.LastPlayedDao
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.media_service.MediaObserver
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.ShareRequest
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.home.data.ExtractMedia
import com.example.tagplayer.home.data.ExtractMediaResult
import com.example.tagplayer.home.data.HandleMediaStore
import com.example.tagplayer.main.presentation.MainViewModel
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.main.presentation.navigation.Screen
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            MediaDatabase::class.java,
            ContextCompat.getString(androidContext(), R.string.database_name)
        ).build()
    }
    single<SongsDao> { get<MediaDatabase>().songsDao }
    single<TagsDao> { get<MediaDatabase>().tagsDao }
    single<LastPlayedDao> { get<MediaDatabase>().lastPlayed }

    single<ForegroundWrapper> {
        ForegroundWrapper.Base(WorkManager.getInstance(androidContext()))
    }
    single<ManageResources.All> { ManageResources.Base(androidContext()) }
    single<ManageResources.Strings> { get<ManageResources.All>() }
    single<ManageResources.SongIdError> { get<ManageResources.All>() }
    single<HandleError<DomainError, String>> { HandleError.Presentation(androidContext()) }
    single<ShareRequest> { ShareRequest.Base(androidContext()) }
    single<HandleMediaStore> {
        val songsDao = get<SongsDao>()
        HandleMediaStore.Base(
            ExtractMedia.Base(androidContext().contentResolver),
            ExtractMediaResult.Mapper.Base(songsDao),
            songsDao
        )
    }
    single {
        MediaObserver(
            Handler(Looper.getMainLooper()),
            get()
        )
    }

    single<Navigation.Mutable> { Navigation.Base }
    single<Navigation.Navigate> { get<Navigation.Mutable>() }
    single<CustomObservable.All<Screen>> { Navigation.Base }
    factory<RunAsync> { RunAsync.Base(DispatcherList.Base) }
    factory<HandleDeath> { HandleDeath.Base() }

    viewModel { MainViewModel(get()) }

    worker { params ->
        MediaWorker(
            androidContext(),
            params.get(),
            get()
        )
    }
    worker { params ->
        PlaySongWorker(
            androidContext(),
            params.get()
        )
    }
    worker { params ->
        FetchSongWorker(
            androidContext(),
            params.get(),
            get()
        )
    }
}