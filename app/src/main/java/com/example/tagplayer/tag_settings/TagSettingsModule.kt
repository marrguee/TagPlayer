package com.example.tagplayer.tag_settings

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.tag_settings.data.TagSettingsCacheDatasource
import com.example.tagplayer.tag_settings.data.TagSettingsRepositoryImpl
import com.example.tagplayer.tag_settings.domain.TagDomain
import com.example.tagplayer.tag_settings.domain.TagSettingsInteractor
import com.example.tagplayer.tag_settings.presentation.Selected
import com.example.tagplayer.tag_settings.presentation.TagSettingsObservable
import com.example.tagplayer.tag_settings.presentation.TagSettingsResponse.TagSettingsResponseMapper
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi
import com.example.tagplayer.tag_settings.presentation.TagSettingsViewModel

interface TagSettingsModule : Module<TagSettingsViewModel> {
    class Base(
        private val core: Core,
        private val selectedTag: Selected<TagSettingsUi>,
        private val clear: () -> Unit
    ) : TagSettingsModule {
        override fun create(): TagSettingsViewModel {
            val tagSettingsCacheDatasource: TagSettingsCacheDatasource.Base =
                TagSettingsCacheDatasource.Base(core.mediaDatabase())
            val tagSettingsRepositoryImpl = TagSettingsRepositoryImpl(
                HandleError.Domain,
                core.foregroundWrapper(),
                tagSettingsCacheDatasource,
                SongTag.Mapper.ToDomain
            )
            val tagSettingsInteractor: TagSettingsInteractor = TagSettingsInteractor.Base(
                tagSettingsRepositoryImpl,
                TagDomain.Mapper.ToUi,
                HandleError.Presentation,
            )
            val observable = TagSettingsObservable()
            val responseTagSettingsMapper = TagSettingsResponseMapper.Base(
                observable,
                DispatcherList.Base
            )
            return TagSettingsViewModel(
                DispatcherList.Base,
                tagSettingsInteractor,
                observable,
                selectedTag,
                responseTagSettingsMapper,
                Navigation.Base,
                clear
            )
        }
    }
}