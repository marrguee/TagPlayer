package com.example.tagplayer.all.presentation

import com.example.tagplayer.all.domain.AllInteractor
import com.example.tagplayer.all.domain.AllResponse.AllResponseMapper
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.AllCacheDatasource
import com.example.tagplayer.all.data.AllRepositoryImpl
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList

interface AllModule : Module<AllFeatureViewModel> {
    class Base(private val core: Core) : AllModule {

        override fun create(): AllFeatureViewModel {
            val allCommunication = Communication.AllCommunication()
            val responseAllMapper = AllResponseMapper.Base(
                allCommunication,
                DispatcherList.Base
            )
            val allCacheDatasource: AllCacheDatasource.Base =
                AllCacheDatasource.Base(core.mediaDatabase())
            val allRepositoryImpl = AllRepositoryImpl(
                HandleError.Domain,
                core.foregroundWrapper(),
                allCacheDatasource,
                Song.Mapper.ToDomain
            )
            val allInteractor: AllInteractor = AllInteractor.Base(
                allRepositoryImpl,
                HandleError.Presentation,
                SongDomain.Mapper.ToPresentation,
            )
            return AllFeatureViewModel(
                allInteractor,
                allCommunication,
                responseAllMapper
            )
        }
    }
}