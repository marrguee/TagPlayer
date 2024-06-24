package com.example.tagplayer.core

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.tagplayer.all.data.ExtractMedia
import com.example.tagplayer.core.data.SongData
import com.example.tagplayer.all.domain.DispatcherList
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.Interactor
import com.example.tagplayer.all.domain.Response.Mapper
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.all.presentation.AllState
import com.example.tagplayer.core.data.BaseRepositoryImpl
import com.example.tagplayer.core.data.CacheDatasource
import com.example.tagplayer.core.data.MediaDatabase
import com.example.tagplayer.core.data.MediaStoreHandler
import com.example.tagplayer.core.data.ObserveMediaBroadcast
import com.example.tagplayer.core.data.ProvideMediaStoreHandler
import com.example.tagplayer.core.data.SongsDao
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.main.domain.ManageResources

interface Core : ProvideMediaStoreHandler, ManageResources.Provide {
    fun allInteractor() : Interactor
    fun runAsync() : RunAsync
    fun responseMapper() : Mapper
    fun communication() : Communication<AllState>
    fun songsDao() : SongsDao
    fun observeMediaBroadcast() : ObserveMediaBroadcast

    class Base(
        context: Context,
        contentResolver: ContentResolver
    ): Core {
        private val mediaDatabase = Room.databaseBuilder(
            context,
            MediaDatabase::class.java,
            "MediaDatabase.db"
        ).build()

        private val cacheDatasource: CacheDatasource =
            CacheDatasource.Base(mediaDatabase)

        private val repository: BaseRepositoryImpl = BaseRepositoryImpl(
            cacheDatasource,
            ForegroundWrapper.Base(WorkManager.getInstance(context)),
            HandleError.Domain,
            SongData.Mapper.ToDomain
        )
        private val allInteractor: Interactor = Interactor.Base(
            repository,
            HandleError.Presentation,
            SongDomain.Mapper.ToPresentation
        )

        private val dispatcherList: DispatcherList = DispatcherList.Base()
        private val runAsync = RunAsync.Base(dispatcherList)
        private val communication = Communication.Base()
        private val responseMapper = Mapper.AllMapper(communication, dispatcherList)
        private val mediaStoreHandler = MediaStoreHandler.Base(
            ExtractMedia.Base(contentResolver),
            mediaDatabase.songsDao
        )

        private val observeMediaBroadcast = ObserveMediaBroadcast(dispatcherList, songsDao())
        private val manageResources = ManageResources.Base(context)
        override fun allInteractor() = allInteractor
        override fun runAsync() = runAsync
        override fun responseMapper() = responseMapper
        override fun communication() = communication
        override fun songsDao() = mediaDatabase.songsDao
        override fun manageRecourses() = manageResources

        override fun observeMediaBroadcast() = observeMediaBroadcast

        override fun mediaStoreHandler() = mediaStoreHandler

    }

}
