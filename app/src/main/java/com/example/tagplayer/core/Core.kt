package com.example.tagplayer.core

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.tagplayer.all.data.ExtractMedia
import com.example.tagplayer.core.data.SongData
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.AllInteractor
import com.example.tagplayer.all.domain.AllResponse.AllMapper
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
import com.example.tagplayer.core.data.SongLastPlayedCrossRef
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.history.domain.HistoryInteractor
import com.example.tagplayer.history.domain.HistoryResponse.HistoryMapper
import com.example.tagplayer.history.presentation.HistoryState
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.domain.RunAsync

interface Core : ProvideMediaStoreHandler, ManageResources.Provide {
    fun allInteractor() : AllInteractor
    fun historyInteractor() : HistoryInteractor
    fun runAsync() : RunAsync
    fun responseAllMapper() : AllMapper
    fun responseHistoryMapper() : HistoryMapper
    fun allCommunication() : Communication<AllState>
    fun historyCommunication() : Communication<HistoryState>
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
            SongData.Mapper.ToDomain,
            SongLastPlayedCrossRef.Mapper.ToDomain
        )
        private val allInteractor: AllInteractor = AllInteractor.Base(
            repository,
            HandleError.Presentation,
            SongDomain.Mapper.ToPresentation
        )
        private val historyInteractor: HistoryInteractor = HistoryInteractor.Base(
            repository,
            HandleError.Presentation
        )

        private val dispatcherList: DispatcherList = DispatcherList.Base()
        private val runAsync = RunAsync.Base(dispatcherList)
        private val allCommunication = Communication.AllCommunication()
        private val historyCommunication = Communication.HistoryCommunication()
        private val responseAllMapper = AllMapper.Base(allCommunication, dispatcherList)
        private val responseHistoryMapper = HistoryMapper.Base(historyCommunication, dispatcherList)
        private val mediaStoreHandler = MediaStoreHandler.Base(
            ExtractMedia.Base(contentResolver),
            mediaDatabase.songsDao
        )

        private val observeMediaBroadcast = ObserveMediaBroadcast(dispatcherList, songsDao())
        private val manageResources = ManageResources.Base(context)
        override fun allInteractor() = allInteractor
        override fun historyInteractor() = historyInteractor
        override fun runAsync() = runAsync
        override fun responseAllMapper() = responseAllMapper
        override fun responseHistoryMapper() = responseHistoryMapper
        override fun allCommunication() = allCommunication
        override fun historyCommunication() = historyCommunication
        override fun songsDao() = mediaDatabase.songsDao
        override fun manageRecourses() = manageResources
        override fun observeMediaBroadcast() = observeMediaBroadcast
        override fun mediaStoreHandler() = mediaStoreHandler

    }

}
