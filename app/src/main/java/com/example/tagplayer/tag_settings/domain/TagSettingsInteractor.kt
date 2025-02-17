package com.example.tagplayer.tag_settings.domain

import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tag_settings.presentation.TagSettingsResponse
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi
import kotlinx.coroutines.flow.map

interface TagSettingsInteractor {
    fun tags() : TagSettingsResponse
    suspend fun removeTag(id: Long)

    class Base(
        private val repository: TagSettingsRepository<TagDomain>,
        private val tagModelMapperToUi: TagDomain.Mapper<TagSettingsUi>,
        private val handleError: HandleError.Presentation,
    ) : TagSettingsInteractor {

        override fun tags(): TagSettingsResponse = try {
            TagSettingsResponse.Success(
                repository.tags().map { list ->
                    list.map { it.map(tagModelMapperToUi) }
                }
            )
        } catch (e: DomainError) {
            TagSettingsResponse.Error(handleError.handle(e))
        }

        override suspend fun removeTag(id: Long) {
            repository.removeTag(id)
        }
    }
}