package com.example.tagplayer.tag_settings.domain

import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.tag_settings.presentation.TagSettingsResponse
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi
import kotlinx.coroutines.flow.map

interface TagSettingsInteractor {
    fun tags(): TagSettingsResponse
    suspend fun remove(id: Long) : TagSettingsResponse

    class Base(
        private val repository: TagSettingsRepository<TagSettingsDomain>,
        private val mapper: TagSettingsDomain.Mapper<TagSettingsUi>,
        private val handleResponse: HandleResponse.All<TagSettingsResponse>,
    ) : TagSettingsInteractor {

        override fun tags(): TagSettingsResponse = handleResponse.handle {
            TagSettingsResponse.Success(
                repository.tags().map { list -> list.map { it.map(mapper) } }
            )
        }

        override suspend fun remove(id: Long) = handleResponse.handleAsyncEmpty {
            repository.remove(id)
        }
    }
}