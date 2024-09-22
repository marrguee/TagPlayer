package com.example.tagplayer.tagsettings.presentation

import androidx.lifecycle.MutableLiveData
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.tagsettings.data.TagSettingsCacheDatasource
import com.example.tagplayer.tagsettings.data.TagSettingsRepositoryImpl
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.tagsettings.domain.TagDomain
import com.example.tagplayer.tagsettings.domain.TagSettingsInteractor
import com.example.tagplayer.tagsettings.presentation.TagSettingsResponse.TagSettingsResponseMapper

interface TagSettingsModule : Module<TagSettingsViewModel> {
    class Base(
        private val core: Core,
        private val selectedTagLiveData: MutableLiveData<TagSettingsUi?>,
        private val clear: () -> Unit
    ) : TagSettingsModule {
        override fun create(): TagSettingsViewModel {
            val tagSettingsCacheDatasource: TagSettingsCacheDatasource.Base =
                TagSettingsCacheDatasource.Base(core.mediaDatabase())
            val tagSettingsRepositoryImpl = TagSettingsRepositoryImpl(
                HandleError.Domain,
                core.foregroundWrapper(),
                tagSettingsCacheDatasource,
                SongTag.Mapper.ToDomain,
                TagDomain.Mapper.ToData
            )
            val tagSettingsInteractor: TagSettingsInteractor = TagSettingsInteractor.Base(
                tagSettingsRepositoryImpl,
                TagDomain.Mapper.ToUi,
                SongTag.Mapper.ToDomain,
                HandleError.Presentation,
            )
            val observable = TagSettingsObservable()
            val responseTagSettingsMapper = TagSettingsResponseMapper.Base(
                observable,
                DispatcherList.Base
            )
            return TagSettingsViewModel(
                tagSettingsInteractor,
                observable,
                selectedTagLiveData,
                responseTagSettingsMapper,
                Navigation.Base,
                clear
            )
        }
    }
}