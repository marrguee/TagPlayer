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
import com.example.tagplayer.all.domain.AllResponse.AllResponseMapper
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
import com.example.tagplayer.history.domain.SongHistoryInteractor
import com.example.tagplayer.history.domain.HistoryResponse.HistoryResponseMapper
import com.example.tagplayer.history.presentation.HistoryState
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.domain.RunAsync
import com.example.tagplayer.search.data.SearchHistory
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse.SearchResponseMapper
import com.example.tagplayer.search.domain.SearchState

interface Core : ProvideMediaStoreHandler, ManageResources.Provide {
    fun allInteractor() : AllInteractor
    fun historyInteractor() : SongHistoryInteractor
    fun searchInteractor() : SearchInteractor
    fun runAsync() : RunAsync
    fun responseAllMapper() : AllResponseMapper
    fun responseHistoryMapper() : HistoryResponseMapper
    fun responseSearchMapper() : SearchResponseMapper
    fun allCommunication() : Communication<AllState>
    fun historyCommunication() : Communication<HistoryState>
    fun searchCommunication() : Communication<SearchState>
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
            SongLastPlayedCrossRef.Mapper.ToDomain,
            SearchHistory.Mapper.Base
        )
        private val allInteractor: AllInteractor = AllInteractor.Base(
            repository,
            HandleError.Presentation,
            SongDomain.Mapper.ToPresentation,
        )
        private val historyInteractor: SongHistoryInteractor = SongHistoryInteractor.Base(
            repository,
            HandleError.Presentation,
        )
        private val searchInteractor: SearchInteractor = SearchInteractor.Base(
            repository,
            HandleError.Presentation,
            SearchDomain.Mapper.ToPresentation,
            SongDomain.Mapper.ToPresentation
        )

        private val dispatcherList: DispatcherList = DispatcherList.Base()
        private val runAsync = RunAsync.Base(dispatcherList)
        private val allCommunication = Communication.AllCommunication()
        private val historyCommunication = Communication.HistoryCommunication()
        private val searchCommunication = Communication.SearchCommunication()
        private val responseAllMapper = AllResponseMapper.Base(allCommunication, dispatcherList)
        private val responseHistoryMapper = HistoryResponseMapper.Base(historyCommunication, dispatcherList)
        private val responseSearchMapper = SearchResponseMapper.Base(searchCommunication)
        private val mediaStoreHandler = MediaStoreHandler.Base(
            ExtractMedia.Base(contentResolver),
            mediaDatabase.songsDao
        )

        private val observeMediaBroadcast = ObserveMediaBroadcast(dispatcherList, songsDao())
        private val manageResources = ManageResources.Base(context)
        override fun allInteractor() = allInteractor
        override fun historyInteractor() = historyInteractor
        override fun searchInteractor() = searchInteractor

        override fun runAsync() = runAsync
        override fun responseAllMapper() = responseAllMapper
        override fun responseHistoryMapper() = responseHistoryMapper
        override fun responseSearchMapper() = responseSearchMapper

        override fun allCommunication() = allCommunication
        override fun historyCommunication() = historyCommunication
        override fun searchCommunication() = searchCommunication

        override fun songsDao() = mediaDatabase.songsDao
        override fun manageRecourses() = manageResources
        override fun observeMediaBroadcast() = observeMediaBroadcast
        override fun mediaStoreHandler() = mediaStoreHandler

    }

}
