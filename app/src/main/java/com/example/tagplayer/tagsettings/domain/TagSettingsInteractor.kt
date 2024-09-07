package com.example.tagplayer.tagsettings.domain

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tagsettings.presentation.TagSettingsResponse
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi
import kotlinx.coroutines.flow.map

interface TagSettingsInteractor {
    fun tags() : TagSettingsResponse
    suspend fun removeTag(id: Long)

    class Base(
        private val repository: TagSettingsRepository<TagDomain>,
        private val tagModelMapperToUi: TagDomain.Mapper<TagSettingsUi>,
        private val tagModelMapperToDomain: SongTag.Mapper<TagDomain>,
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