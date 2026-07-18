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
import com.example.tagplayer.tag_settings.domain.TagSettingsRepository
import com.example.tagplayer.tag_settings.domain.errors.TagSettingsHandleDomainError
import com.example.tagplayer.tag_settings.presentation.TagSettingsObservable
import com.example.tagplayer.tag_settings.presentation.TagSettingsResponse
import com.example.tagplayer.tag_settings.presentation.TagSettingsViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.viewmodel.scope.viewModelScope

@OptIn(KoinExperimentalAPI::class)
val tagSettingsModule = module {
    @OptIn(KoinViewModelScopeApi::class)
    viewModelScope {
        scoped<TagSettingsObservable> { TagSettingsObservable() }

        scoped<TagSettingsCacheDatasource> {
            TagSettingsCacheDatasource.Base(
                tagsDao = get<TagsDao>()
            )
        }

        scoped<TagSettingsRepository<TagSettingsDomain>> {
            TagSettingsRepositoryImpl(
                foregroundWrapper = get<ForegroundWrapper>(),
                handleTry = HandleTry.Base(
                    handleError = TagSettingsHandleDomainError.Base
                ),
                cacheDatasource = get<TagSettingsCacheDatasource>(),
                mapper = SongTag.Mapper.ToDomain
            )
        }

        scoped<TagSettingsInteractor> {
            TagSettingsInteractor.Base(
                repository = get<TagSettingsRepository<TagSettingsDomain>>(),
                mapper = TagSettingsDomain.Mapper.ToUi,
                handleResponse = HandleResponse.WithEmpty(
                    empty = TagSettingsResponse.Empty,
                    handleError = get<HandleError<DomainError, String>>(),
                    errorResponse = { error, mapper ->
                        TagSettingsResponse.Error(message = mapper.handle(error))
                    }
                )
            )
        }

        scoped<TagSettingsResponse.Mapper> {
            TagSettingsResponse.Mapper.Base(
                observable = get<TagSettingsObservable>(),
                dispatcherList = DispatcherList.Base
            )
        }

        viewModel {
            TagSettingsViewModel(
                runAsync = get<RunAsync>(),
                interactor = get<TagSettingsInteractor>(),
                observable = get<TagSettingsObservable>(),
                mapper = get<TagSettingsResponse.Mapper>(),
                navigation = get<Navigation.Navigate>()
            )
        }
    }
}