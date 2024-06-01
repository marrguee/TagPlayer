package com.example.tagplayer.core

import android.content.ContentResolver
import com.example.tagplayer.all.data.ExtractMedia
import com.example.tagplayer.all.domain.AllRepository
import com.example.tagplayer.all.domain.DispatcherList
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.Interactor
import com.example.tagplayer.all.domain.ModelMapper
import com.example.tagplayer.all.domain.Response.Mapper
import com.example.tagplayer.all.domain.TrackDomain
import com.example.tagplayer.all.presentation.AllState

interface Core {
    fun allInteractor() : Interactor
    fun runAsync() : RunAsync
    fun responseMapper() : Mapper
    fun communication() : Communication<AllState>

    class Base(
        contentResolver: ContentResolver
    ): Core {
        private val cacheDatasource: CacheDatasource =
            CacheDatasource.Base(ExtractMedia.Base(contentResolver))

        private val repository: AllRepository<TrackDomain> = BaseRepositoryImpl(
            cacheDatasource,
            HandleError.Domain,
            ModelMapper.ToDomain
        )
        private val allInteractor: Interactor = Interactor.Base(
            repository,
            HandleError.Presentation,
            ModelMapper.ToPresentation
        )

        private val dispatcherList: DispatcherList = DispatcherList.Base()
        private val runAsync = RunAsync.Base(dispatcherList)
        private val communication = Communication.Base()
        private val responseMapper = Mapper.AllMapper(communication)
        override fun allInteractor() = allInteractor
        override fun runAsync() = runAsync
        override fun responseMapper() = responseMapper
        override fun communication() = communication

    }

}
