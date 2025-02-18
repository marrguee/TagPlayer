package com.example.tagplayer.tag_settings

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.tag_settings.data.TagSettingsCacheDatasource
import com.example.tagplayer.tag_settings.data.TagSettingsRepositoryImpl
import com.example.tagplayer.tag_settings.domain.TagSettingsDomain
import com.example.tagplayer.tag_settings.domain.errors.TagSettingsHandleDomainError
import com.example.tagplayer.tag_settings.domain.TagSettingsInteractor
import com.example.tagplayer.tag_settings.presentation.TagSettingsObservable
import com.example.tagplayer.tag_settings.presentation.TagSettingsResponse
import com.example.tagplayer.tag_settings.presentation.TagSettingsResponse.Mapper
import com.example.tagplayer.tag_settings.presentation.TagSettingsViewModel

interface TagSettingsModule : Module<TagSettingsViewModel> {
    class Base(
        core: Core,
        private val clear: ClearViewModel
    ) : TagSettingsModule {
        private val observable = TagSettingsObservable()
        private val tagSettingsCacheDatasource: TagSettingsCacheDatasource.Base =
            TagSettingsCacheDatasource.Base(core.tagDao())
        private val tagSettingsRepositoryImpl = TagSettingsRepositoryImpl(
            core.foregroundWrapper(),
            HandleTry.Base(TagSettingsHandleDomainError.Base),
            tagSettingsCacheDatasource,
            SongTag.Mapper.ToDomain
        )
        private val tagSettingsInteractor: TagSettingsInteractor = TagSettingsInteractor.Base(
            tagSettingsRepositoryImpl,
            TagSettingsDomain.Mapper.ToUi,
            HandleResponse.WithEmpty(
                TagSettingsResponse.Empty,
                core.handlePresentationError()
            ) { e, handleError ->
                TagSettingsResponse.Error(handleError.handle(e))
            },
        )
        private val responseTagSettingsMapper = Mapper.Base(
            observable,
            DispatcherList.Base
        )

        override fun create(): TagSettingsViewModel = TagSettingsViewModel(
            clear,
            RunAsync.Base(DispatcherList.Base),
            tagSettingsInteractor,
            observable,
            responseTagSettingsMapper,
            Navigation.Base,
        )
    }
}