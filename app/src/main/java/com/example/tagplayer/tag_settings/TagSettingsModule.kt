package com.example.tagplayer.tag_settings

import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.tag_settings.data.TagSettingsCacheDatasource
import com.example.tagplayer.tag_settings.data.TagSettingsRepositoryImpl
import com.example.tagplayer.tag_settings.domain.TagSettingsDomain
import com.example.tagplayer.tag_settings.domain.TagSettingsInteractor
import com.example.tagplayer.tag_settings.domain.errors.TagSettingsHandleDomainError
import com.example.tagplayer.tag_settings.presentation.TagSettingsObservable
import com.example.tagplayer.tag_settings.presentation.TagSettingsResponse
import com.example.tagplayer.tag_settings.presentation.TagSettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val tagSettingsModule = module {
    viewModel {
        val observable = TagSettingsObservable()
        val repository = TagSettingsRepositoryImpl(
            get<ForegroundWrapper>(),
            HandleTry.Base(TagSettingsHandleDomainError.Base),
            TagSettingsCacheDatasource.Base(get<TagsDao>()),
            SongTag.Mapper.ToDomain
        )
        val interactor = TagSettingsInteractor.Base(
            repository,
            TagSettingsDomain.Mapper.ToUi,
            HandleResponse.WithEmpty(
                TagSettingsResponse.Empty,
                get<HandleError<DomainError, String>>()
            ) { error, mapper -> TagSettingsResponse.Error(mapper.handle(error)) }
        )

        TagSettingsViewModel(
            get<RunAsync>(),
            interactor,
            observable,
            TagSettingsResponse.Mapper.Base(observable, DispatcherList.Base),
            get<Navigation.Navigate>()
        )
    }
}